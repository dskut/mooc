#! /usr/bin/env ruby

def palindrome?(str)
    str = str.gsub(/\W/, "").downcase
    return str == str.reverse
end

def count_words(str)
    h = {}
    str.scan(/\b(\w+)\b/) do |match|
        match.each do |w| 
            word = w.downcase
            if h.has_key?(word) then
                h[word] += 1
            else
                h[word] = 1
            end
        end
    end
    return h
end


