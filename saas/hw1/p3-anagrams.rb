#! /usr/bin/ruby

def combine_anagrams(words)
    res = {}
    words.each do |word|
        sorted = word.downcase.chars.sort.join
        if res.has_key? sorted then
            res[sorted].push word
        else
            res[sorted] = [word]
        end
    end
    return res.values
end

