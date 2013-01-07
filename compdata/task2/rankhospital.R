rankhospital <- function(state, outcome, rank = "best") {  
  if (outcome == "heart attack") {
    outcome_index <- 11
  } else if (outcome == "heart failure") {
    outcome_index <- 17
  } else if (outcome == "pneumonia") {
    outcome_index <- 23
  } else {
    stop("invalid outcome")
  }
  
  data <- read.csv("outcome-of-care-measures.csv", colClasses="character")
  data[, outcome_index] <- suppressWarnings(as.numeric(data[, outcome_index]))
  data <- data[!is.na(data[, outcome_index]), ]
  
  if (!(state %in% data$State)) {
    stop("invalid state")
  }
  
  hospitals <- data[which(data$State == state), ]
  sorted <- hospitals[order(hospitals[, outcome_index], hospitals$Hospital.Name), ]
  
  if (rank == "best") {
    sorted[1,]$Hospital.Name
  } else if (rank == "worst") {
    tail(sorted, n=1)$Hospital.Name
  } else {
    sorted[as.numeric(rank),]$Hospital.Name
  }
}