/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */
    // Max size of string constants
    static int MAX_STR_CONST = 1024;
    private StringBuffer _str = new StringBuffer(); // For assembling string constants
    private int curr_lineno = 1;
    private AbstractSymbol filename;
    private int _commentsLevel = 0;
    int get_curr_lineno() {
	    return curr_lineno;
    }
    void set_filename(String fname) {
        filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
        return filename;
    }
    /**
     * @return: 0 - for success
     *          1 - escaped null
     */
    private int unescapeString() {
        int slashIndex = 0;
        while (true) {
            slashIndex = _str.indexOf("\\", slashIndex);
            if (slashIndex == -1 || slashIndex == _str.length() - 1) {
                break;
            }
            _str.deleteCharAt(slashIndex);
            if (_str.charAt(slashIndex) == 'n') {
                _str.setCharAt(slashIndex, '\n');
            } else if (_str.charAt(slashIndex) == 't') {
                _str.setCharAt(slashIndex, '\t');
            } else if (_str.charAt(slashIndex) == 'f') {
                _str.setCharAt(slashIndex, '\f');
            } else if (_str.charAt(slashIndex) == 'b') {
                _str.setCharAt(slashIndex, '\b');
            } else if (_str.charAt(slashIndex) == '\0') {
                return 1;
            } else if (_str.charAt(slashIndex) == '\\') {
                ++slashIndex;
            }
        }
        return 0;
    }
    private int countEndBackslashes() {
        int res = 0;
        for (int i = _str.length() - 1; i >= 0; --i) {
            if (_str.charAt(i) == '\\') {
                ++res;
            } else {
                break;
            }
        }
        return res;
    }
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING = 3;
	private final int YYINITIAL = 0;
	private final int COMMENT = 1;
	private final int MULTICOMMENT = 2;
	private final int yy_state_dtrans[] = {
		0,
		50,
		60,
		83
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NOT_ACCEPT,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NOT_ACCEPT,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"19:8,20:2,21,20:2,17,19:18,20,19,25,19:5,22,24,23,54,52,18,55,53,26:10,56,5" +
"0,51,48,49,19,60,30,31,32,33,34,11,31,35,36,31:2,37,31,38,39,40,31,41,42,10" +
",43,44,45,31:3,19:4,46,19,3,47,1,16,8,29,47,7,5,47:2,2,47,6,13,14,47,9,4,27" +
",28,15,12,47:3,58,19,59,57,19,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,167,
"0,1,2,3,4,5,1:2,6,7,1:2,8,9,1,10,1:9,11,12,13,12,1:6,12:7,13,12:7,14,1:3,15" +
",1:5,16,17,18,19,13,12,13:8,12,13:5,20,21,1,22,23,24,25,26,27,28,29,30,31,3" +
"2,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,5" +
"7,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,8" +
"2,83,84,85,86,87,88,89,90,91,92,93,94,12,13,95,96,97,98,99,100,101,102,103")[0];

	private int yy_nxt[][] = unpackFromString(104,61,
"1,2,116,156:2,61,118,156,158,156,3,62,160,84,162,156:2,4,5,6,4,7,8,9,10,11," +
"12,164,156,86,157:2,159,157,161,157,85,117,119,87,163,157:4,165,6,156,13,6," +
"14,15,16,17,18,19,20,21,22,23,24,-1:62,156,166,120,156:13,-1:9,156:4,120,15" +
"6:6,166,156:10,-1:14,157:6,121,157:9,-1:9,157:9,121,157:12,-1:30,4,-1:2,4,-" +
"1:58,29,-1:65,30,-1:61,31,-1:62,12,-1:83,32,-1:29,33,-1:29,34,-1:13,156:6,1" +
"44,156:9,-1:9,156:9,144,156:12,-1:14,156:16,-1:9,156:22,-1:14,157:16,-1:9,1" +
"57:22,-1:13,1,80:16,-1,80:3,51,80:39,-1:24,56,-1:36,1,52:20,53,81,54,82,52:" +
"36,-1,156:3,124,156,25,156:4,26,156:5,-1:9,156:3,26,156:8,25,156:3,124,156:" +
"5,-1:14,157:4,27,157:11,-1:9,157:10,27,157:11,-1:14,157:6,143,157:9,-1:9,15" +
"7:9,143,157:12,-1:14,80:16,-1,80:3,-1,80:39,-1:23,55,-1:37,1,57:20,58,57:3," +
"59,57:35,-1,156:10,28,156:5,-1:9,156:3,28,156:18,-1:14,157:3,131,157,63,157" +
":4,64,157:5,-1:9,157:3,64,157:8,63,157:3,131,157:5,-1:14,156:2,138,156,65,1" +
"56:11,-1:9,156:4,138,156:5,65,156:11,-1:14,157:10,66,157:5,-1:9,157:3,66,15" +
"7:18,-1:14,156:9,35,156:6,-1:9,156,35,156:20,-1:14,157:9,67,157:6,-1:9,157," +
"67,157:20,-1:14,156:11,36,156:4,-1:9,156:19,36,156:2,-1:14,157:11,68,157:4," +
"-1:9,157:19,68,157:2,-1:14,156:9,37,156:6,-1:9,156,37,156:20,-1:14,157:9,69" +
",157:6,-1:9,157,69,157:20,-1:14,156:7,38,156:8,-1:9,156:8,38,156:13,-1:14,1" +
"57:5,42,157:10,-1:9,157:12,42,157:9,-1:14,156:13,39,156:2,-1:9,156:14,39,15" +
"6:7,-1:14,157:7,70,157:8,-1:9,157:8,70,157:13,-1:14,156:7,40,156:8,-1:9,156" +
":8,40,156:13,-1:14,157:7,72,157:8,-1:9,157:8,72,157:13,-1:14,41,156:15,-1:9" +
",156:6,41,156:15,-1:14,73,157:15,-1:9,157:6,73,157:15,-1:14,156,43,156:14,-" +
"1:9,156:11,43,156:10,-1:14,157:13,71,157:2,-1:9,157:14,71,157:7,-1:14,156:5" +
",74,156:10,-1:9,156:12,74,156:9,-1:14,157,75,157:14,-1:9,157:11,75,157:10,-" +
"1:14,156:7,44,156:8,-1:9,156:8,44,156:13,-1:14,157:3,76,157:12,-1:9,157:16," +
"76,157:5,-1:14,156:3,45,156:12,-1:9,156:16,45,156:5,-1:14,157:7,77,157:8,-1" +
":9,157:8,77,157:13,-1:14,156:7,46,156:8,-1:9,156:8,46,156:13,-1:14,157:15,7" +
"8,-1:9,157:7,78,157:14,-1:14,156:7,47,156:8,-1:9,156:8,47,156:13,-1:14,157:" +
"3,79,157:12,-1:9,157:16,79,157:5,-1:14,156:15,48,-1:9,156:7,48,156:14,-1:14" +
",156:3,49,156:12,-1:9,156:16,49,156:5,-1:14,156:7,88,156:4,122,156:3,-1:9,1" +
"56:8,88,156:4,122,156:8,-1:14,157:7,89,157:4,133,157:3,-1:9,157:8,89,157:4," +
"133,157:8,-1:14,156:7,90,156:4,92,156:3,-1:9,156:8,90,156:4,92,156:8,-1:14," +
"157:7,91,157:4,93,157:3,-1:9,157:8,91,157:4,93,157:8,-1:14,156:3,94,156:12," +
"-1:9,156:16,94,156:5,-1:14,157:7,95,157:8,-1:9,157:8,95,157:13,-1:14,156:12" +
",96,156:3,-1:9,156:13,96,156:8,-1:14,157:2,139,157:13,-1:9,157:4,139,157:17" +
",-1:14,156:14,142,156,-1:9,156:18,142,156:3,-1:14,157:3,97,157:12,-1:9,157:" +
"16,97,157:5,-1:14,156:3,98,156:12,-1:9,156:16,98,156:5,-1:14,157:3,99,157:1" +
"2,-1:9,157:16,99,157:5,-1:14,156:2,100,156:13,-1:9,156:4,100,156:17,-1:14,1" +
"57:2,101,157:13,-1:9,157:4,101,157:17,-1:14,156:4,146,156:11,-1:9,156:10,14" +
"6,156:11,-1:14,157:14,141,157,-1:9,157:18,141,157:3,-1:14,156:12,102,156:3," +
"-1:9,156:13,102,156:8,-1:14,157:12,103,157:3,-1:9,157:13,103,157:8,-1:14,15" +
"6:7,104,156:8,-1:9,156:8,104,156:13,-1:14,157:12,105,157:3,-1:9,157:13,105," +
"157:8,-1:14,156:16,-1:9,156:2,106,156:14,106,156:4,-1:14,157:4,145,157:11,-" +
"1:9,157:10,145,157:11,-1:14,156,148,156:14,-1:9,156:11,148,156:10,-1:14,157" +
":3,107,157:12,-1:9,157:16,107,157:5,-1:14,156:3,108,156:12,-1:9,156:16,108," +
"156:5,-1:14,157:12,147,157:3,-1:9,157:13,147,157:8,-1:14,156:12,150,156:3,-" +
"1:9,156:13,150,156:8,-1:14,157:7,149,157:8,-1:9,157:8,149,157:13,-1:14,156:" +
"7,152,156:8,-1:9,156:8,152,156:13,-1:14,157,109,157:14,-1:9,157:11,109,157:" +
"10,-1:14,156,110,156:14,-1:9,156:11,110,156:10,-1:14,157:4,111,157:11,-1:9," +
"157:10,111,157:11,-1:14,156:3,112,156:12,-1:9,156:16,112,156:5,-1:14,157:8," +
"151,157:7,-1:9,157:15,151,157:6,-1:14,156:4,114,156:11,-1:9,156:10,114,156:" +
"11,-1:14,157:4,153,157:11,-1:9,157:10,153,157:11,-1:14,156:8,154,156:7,-1:9" +
",156:15,154,156:6,-1:14,157:9,113,157:6,-1:9,157,113,157:20,-1:14,156:4,155" +
",156:11,-1:9,156:10,155,156:11,-1:14,156:9,115,156:6,-1:9,156,115,156:20,-1" +
":14,156,126,156,128,156:12,-1:9,156:11,126,156:4,128,156:5,-1:14,157,123,12" +
"5,157:13,-1:9,157:4,125,157:6,123,157:10,-1:14,156:6,130,156:9,-1:9,156:9,1" +
"30,156:12,-1:14,157,127,157,129,157:12,-1:9,157:11,127,157:4,129,157:5,-1:1" +
"4,156:12,132,156:3,-1:9,156:13,132,156:8,-1:14,157:12,135,157:3,-1:9,157:13" +
",135,157:8,-1:14,156:6,134,156,136,156:7,-1:9,156:9,134,156:5,136,156:6,-1:" +
"14,157:6,137,157:9,-1:9,157:9,137,157:12,-1:14,156:2,140,156:13,-1:9,156:4," +
"140,156:17,-1:13");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    switch(yy_lexical_state) {
	case MULTICOMMENT:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in comment");
    case STRING:
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "EOF in string constant");
    }
    return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -3:
						break;
					case 3:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -4:
						break;
					case 4:
						{}
					case -5:
						break;
					case 5:
						{ return new Symbol(TokenConstants.MINUS); }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -7:
						break;
					case 7:
						{ ++curr_lineno; }
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.MULT); }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -11:
						break;
					case 11:
						{ 
    yybegin(STRING);
    _str = new StringBuffer();
}
					case -12:
						break;
					case 12:
						{
    AbstractSymbol symbol = AbstractTable.inttable.addString(yytext());
    return new Symbol(TokenConstants.INT_CONST, symbol);
}
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.EQ); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.SEMI); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.LT); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.COMMA); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.DIV); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.PLUS); }
					case -19:
						break;
					case 19:
						{ return new Symbol(TokenConstants.DOT); }
					case -20:
						break;
					case 20:
						{ return new Symbol(TokenConstants.COLON); }
					case -21:
						break;
					case 21:
						{ return new Symbol(TokenConstants.NEG); }
					case -22:
						break;
					case 22:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -23:
						break;
					case 23:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -24:
						break;
					case 24:
						{ return new Symbol(TokenConstants.AT); }
					case -25:
						break;
					case 25:
						{ return new Symbol(TokenConstants.IN); }
					case -26:
						break;
					case 26:
						{ return new Symbol(TokenConstants.IF); }
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.FI); }
					case -28:
						break;
					case 28:
						{ return new Symbol(TokenConstants.OF); }
					case -29:
						break;
					case 29:
						{ yybegin(COMMENT); }
					case -30:
						break;
					case 30:
						{ 
    yybegin(MULTICOMMENT); 
    _commentsLevel = 1;
}
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }
					case -32:
						break;
					case 32:
						{ return new Symbol(TokenConstants.DARROW); }
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.LE); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.LET); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.NEW); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.NOT); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.CASE); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.LOOP); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.ELSE); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.ESAC); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.THEN); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.POOL); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.BOOL_CONST, true); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.CLASS); }
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.WHILE); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.BOOL_CONST, false); }
					case -48:
						break;
					case 48:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -49:
						break;
					case 49:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -50:
						break;
					case 50:
						{}
					case -51:
						break;
					case 51:
						{ 
    yybegin(YYINITIAL); 
    ++curr_lineno;
}
					case -52:
						break;
					case 52:
						{}
					case -53:
						break;
					case 53:
						{ ++curr_lineno; }
					case -54:
						break;
					case 54:
						{}
					case -55:
						break;
					case 55:
						{ ++_commentsLevel; }
					case -56:
						break;
					case 56:
						{ 
    --_commentsLevel;
    if (_commentsLevel == 0) {
        yybegin(YYINITIAL);
    }
}
					case -57:
						break;
					case 57:
						{ _str.append(yytext()); }
					case -58:
						break;
					case 58:
						{
    ++curr_lineno;
    if (countEndBackslashes() % 2 != 0) {
        _str.append("\n");
    } else {
        yybegin(YYINITIAL);
        return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
    }
}
					case -59:
						break;
					case 59:
						{
    if (countEndBackslashes() % 2 != 0 ) {
        // quote is escaped
        _str.deleteCharAt(_str.length() - 1);
        _str.append('\"');
    } else {
        yybegin(YYINITIAL);
        int unescapeRes = unescapeString();
        if (unescapeRes == 1) {
            return new Symbol(TokenConstants.ERROR, "String contains escaped null character.");
        }
        if (_str.indexOf("\0") != -1) {
            return new Symbol(TokenConstants.ERROR, "String contains null character.");
        }
        if (_str.length() > MAX_STR_CONST) {
            return new Symbol(TokenConstants.ERROR, "String constant too long");
        }
        String str = _str.toString();
        AbstractSymbol symbol = AbstractTable.inttable.addString(str); 
        return new Symbol(TokenConstants.STR_CONST, symbol); 
    }
}
					case -60:
						break;
					case 61:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -61:
						break;
					case 62:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -62:
						break;
					case 63:
						{ return new Symbol(TokenConstants.IN); }
					case -63:
						break;
					case 64:
						{ return new Symbol(TokenConstants.IF); }
					case -64:
						break;
					case 65:
						{ return new Symbol(TokenConstants.FI); }
					case -65:
						break;
					case 66:
						{ return new Symbol(TokenConstants.OF); }
					case -66:
						break;
					case 67:
						{ return new Symbol(TokenConstants.LET); }
					case -67:
						break;
					case 68:
						{ return new Symbol(TokenConstants.NEW); }
					case -68:
						break;
					case 69:
						{ return new Symbol(TokenConstants.NOT); }
					case -69:
						break;
					case 70:
						{ return new Symbol(TokenConstants.CASE); }
					case -70:
						break;
					case 71:
						{ return new Symbol(TokenConstants.LOOP); }
					case -71:
						break;
					case 72:
						{ return new Symbol(TokenConstants.ELSE); }
					case -72:
						break;
					case 73:
						{ return new Symbol(TokenConstants.ESAC); }
					case -73:
						break;
					case 74:
						{ return new Symbol(TokenConstants.THEN); }
					case -74:
						break;
					case 75:
						{ return new Symbol(TokenConstants.POOL); }
					case -75:
						break;
					case 76:
						{ return new Symbol(TokenConstants.CLASS); }
					case -76:
						break;
					case 77:
						{ return new Symbol(TokenConstants.WHILE); }
					case -77:
						break;
					case 78:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -78:
						break;
					case 79:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -79:
						break;
					case 80:
						{}
					case -80:
						break;
					case 81:
						{}
					case -81:
						break;
					case 82:
						{}
					case -82:
						break;
					case 84:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -83:
						break;
					case 85:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -84:
						break;
					case 86:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -85:
						break;
					case 87:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -86:
						break;
					case 88:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -87:
						break;
					case 89:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -88:
						break;
					case 90:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -89:
						break;
					case 91:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -90:
						break;
					case 92:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -91:
						break;
					case 93:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -92:
						break;
					case 94:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -93:
						break;
					case 95:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -94:
						break;
					case 96:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -95:
						break;
					case 97:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -96:
						break;
					case 98:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -97:
						break;
					case 99:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -98:
						break;
					case 100:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -99:
						break;
					case 101:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -100:
						break;
					case 102:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -101:
						break;
					case 103:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -102:
						break;
					case 104:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -103:
						break;
					case 105:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -104:
						break;
					case 106:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -105:
						break;
					case 107:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -106:
						break;
					case 108:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -107:
						break;
					case 109:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -108:
						break;
					case 110:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -109:
						break;
					case 111:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -110:
						break;
					case 112:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -111:
						break;
					case 113:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -112:
						break;
					case 114:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -113:
						break;
					case 115:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -114:
						break;
					case 116:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -115:
						break;
					case 117:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -116:
						break;
					case 118:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -117:
						break;
					case 119:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -118:
						break;
					case 120:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -119:
						break;
					case 121:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -120:
						break;
					case 122:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -121:
						break;
					case 123:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -122:
						break;
					case 124:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -123:
						break;
					case 125:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -124:
						break;
					case 126:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -125:
						break;
					case 127:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -126:
						break;
					case 128:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -127:
						break;
					case 129:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -128:
						break;
					case 130:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -129:
						break;
					case 131:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -130:
						break;
					case 132:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -131:
						break;
					case 133:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -132:
						break;
					case 134:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -133:
						break;
					case 135:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -134:
						break;
					case 136:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -135:
						break;
					case 137:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -136:
						break;
					case 138:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -137:
						break;
					case 139:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -138:
						break;
					case 140:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -139:
						break;
					case 141:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -140:
						break;
					case 142:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -141:
						break;
					case 143:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -142:
						break;
					case 144:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -143:
						break;
					case 145:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -144:
						break;
					case 146:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -145:
						break;
					case 147:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -146:
						break;
					case 148:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -147:
						break;
					case 149:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -148:
						break;
					case 150:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -149:
						break;
					case 151:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -150:
						break;
					case 152:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -151:
						break;
					case 153:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -152:
						break;
					case 154:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -153:
						break;
					case 155:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -154:
						break;
					case 156:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -155:
						break;
					case 157:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -156:
						break;
					case 158:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -157:
						break;
					case 159:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -158:
						break;
					case 160:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -159:
						break;
					case 161:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -160:
						break;
					case 162:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -161:
						break;
					case 163:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -162:
						break;
					case 164:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -163:
						break;
					case 165:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext());
    return new Symbol(TokenConstants.TYPEID, symbol); 
}
					case -164:
						break;
					case 166:
						{
    AbstractSymbol symbol = AbstractTable.idtable.addString(yytext()); 
    return new Symbol(TokenConstants.OBJECTID, symbol); 
}
					case -165:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
