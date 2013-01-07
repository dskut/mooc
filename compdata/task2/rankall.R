rankall <- function(outcome, rank = "best") {  
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
  
  states <- sort(unique(data$State))
  N <- length(states)
  
  ranked_hospitals <- states
  i = 0
  
  for (state in states) {
    hospitals <- data[which(data$State == state), ]
    sorted <- hospitals[order(hospitals[, outcome_index], hospitals$Hospital.Name), ]
    
    if (rank == "best") {
      res <- sorted[1,]
    } else if (rank == "worst") {
      res <- tail(sorted, n=1)
    } else {
      res <- sorted[as.numeric(rank),]
    }
    
    i <- i + 1
    ranked_hospitals[i] <- res$Hospital.Name
  }

  dfRes <- data.frame(hospital=ranked_hospitals, state=states)
  row.names(dfRes) <- states
  dfRes
}