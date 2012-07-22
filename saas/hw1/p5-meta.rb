
class Class
    def attr_accessor_with_history(attr_name)
        attr_name = attr_name.to_s
        attr_reader attr_name
        attr_reader attr_name + "_history"
        class_eval %Q"
            def #{attr_name}= (val) 
                if @#{attr_name}.nil? then
                    @#{attr_name}_history = [nil, val]
                else
                    @#{attr_name}_history.push val
                end
                @#{attr_name} = val
            end
        "
    end
end

class Numeric
    @@currencies = {'dollar' => 1, 'yen' => 0.013, 'euro' => 1.292, 'rupee' => 0.019}

    def method_missing(method_id)
        singular_currency = method_id.to_s.gsub(/s$/, '')
        if @@currencies.has_key?(singular_currency)
            self * @@currencies[singular_currency]
        else
            super
        end
    end

    def in(curr)
        curr_s = curr.to_s.gsub(/s$/, '');
        if @@currencies.has_key?(curr_s)
            self / @@currencies[curr_s]
        else
            super
        end
    end
end

class String
    def palindrome?
        str = self.gsub(/\W/, "").downcase
        return str == str.reverse
    end
end

module Enumerable
    def palindrome?
        return self == self.reverse
    end
end
