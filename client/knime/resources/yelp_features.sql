--features
DROP TABLE if exists features ;

CREATE TABLE features(     
     stars                  FLOAT,
     text                   INT,
     vote_funny             INT,
     vote_useful            INT,
     vote_cool              INT,
     business_id            STRING,
     business_city          STRING,
     business_state         STRING,
     business_open          INT,
     business_review_count  INT,
     business_stars         FLOAT,
     user_review_count      INT,
     user_average_stars     FLOAT)
  ROW FORMAT DELIMITED FIELDS TERMINATED BY "," ESCAPED BY '\\' STORED AS TEXTFILE
  LOCATION '/user/hive/yelpdb/warehouse/features';

INSERT OVERWRITE TABLE features
  SELECT r.stars, IF(r.text != null, 1, 0), r.vote_funny, r.vote_useful, r.vote_cool, 
         b.business_id, b.city, b.state, b.open, b.review_count, b.stars,
         u.review_count, u.average_stars
  FROM review r 
    JOIN business b ON(r.business_id = b.business_id)
    JOIN user u     ON(r.user_id = u.user_id);
