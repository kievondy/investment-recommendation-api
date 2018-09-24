
CREATE TABLE investment_recommendation (
   id INT IDENTITY PRIMARY KEY,
   investment_name VARCHAR(100) NOT NULL,
   risk_score INT NOT NULL,
   investment_return_score INT NOT NULL
);


CREATE TABLE client_portfolio (
   id INT IDENTITY PRIMARY KEY,
   client_name VARCHAR(100) NOT NULL,
   risk_score INT NOT NULL
);

