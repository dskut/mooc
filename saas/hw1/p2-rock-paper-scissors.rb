#! /usr/bin/ruby

class WrongNumberOfPlayersError < StandardError ; end

class NoSuchStrategy < StandardError ; end

def rps_game_winner(game)
    raise WrongNumberOfPlayersError unless game.length == 2
    first = game[0]
    second = game[1]
    first_str = first[1].downcase
    second_str = second[1].downcase
    raise NoSuchStrategy unless "rps".include? first_str and "rps".include? second_str
    return first if first_str == second_str
    if first_str == "r" then
        return second_str == "s" ? first : second
    elsif first_str == "p" then
        return second_str == "r" ? first : second
    else
        return second_str == "p" ? first : second
    end
end

def rps_tournament_winner(tournament)
    if tournament[0][0].class == String then
        return rps_game_winner(tournament)
    else
        return rps_tournament_winner [rps_tournament_winner(tournament[0]), 
                                      rps_tournament_winner(tournament[1])]
    end
end
