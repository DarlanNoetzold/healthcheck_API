# healthcheck_API
 
Estudos para criar uma API que prevê quedas e problemas de performance. Atualmente a API está capturando os dados de metricas para o dataset.

https://miro.com/app/board/uXjVNq0eP1I=/?utm_source=showme&utm_campaign=cpa

API Endpoint Setup:
- Define endpoints for registering APIs to monitor
- Define endpoints for retrieving performance predictions and historical data

Data Collection:
- Implement a scheduler to send periodic requests to the registered APIs
- Collect metrics like response time, status code, etc.

Database Storage:
- Design a database schema to store API metrics
- Insert collected data into the database

Data Analysis and Prediction:
- Implement a data analysis algorithm or use a machine learning model
- Train the model with historical data
- Predict future performance issues based on this analysis

Notification System:
- Implement a system to send alerts based on predictions
- Define criteria for alerts (e.g., likelihood of downtime)

Security and Authentication:
- Implement authentication for API usage
- Ensure all data transmission is secured

User Interface (Optional):
- Design a UI for registering and monitoring APIs
- Show historical data and predictions
