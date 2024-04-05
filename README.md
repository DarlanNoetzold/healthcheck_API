# HealthCheck Analytics Platform

## Overview

The HealthCheck Analytics Platform is a comprehensive solution for monitoring and analyzing API health metrics. This project employs a microservices architecture to collect, analyze, and predict health metrics, enabling early identification of issues and performance optimization.

The system comprises several key components:

- **HealthCheck Collector API**: Extracts metrics from another API every 15 minutes, storing the results in a PostgreSQL database. A procedure is then executed to update an `is_alert` flag, analyzing whether the record falls outside the expected norm.

- **Data Processing Scripts**: Includes `extract_dataset_from_database`, optimized with Cython for improved performance, and `data_preparation` using Cnumpy and Cython for data preparation.

- **Hyperparameter Tuning and Model Training**: Uses GridSearchCV or RandomizedSearchCV to explore parameter combinations, training regression and classification models for each collected metric.

- **Prediction API (`predict_next_value`)**: Allows for the prediction of metric values and the `is_alert` flag for the next 15 minutes based on the provided data.

- **HealthCheck-gate**: A Spring API that acts as an intermediary, processing data, saving records and accuracies in the database, and communicating with the prediction API.

- **HealthCheck Dashboard**: A React frontend application that displays metrics, prediction results, and allows user interaction for data forecasting.

## Deployment

The installation process will be detailed in the future, using docker to facilitate installation or with a manual setup, using the [start.sh](https://github.com/DarlanNoetzold/healthcheck_API/blob/main/start_all.sh) file.

## Results

Training results on local architecture:
https://github.com/DarlanNoetzold/healthcheck_API/blob/main/model_accuracies.csv

Training results on Google Colab Pro:
https://github.com/DarlanNoetzold/healthcheck_API/blob/main/model_accuracies_Google_pro.csv

### Graphics:

![image](https://github.com/DarlanNoetzold/healthcheck_API/assets/41628589/1862af51-27a4-4727-9b99-79ea45405e6d)

![image](https://github.com/DarlanNoetzold/healthcheck_API/assets/41628589/322edd39-0ac8-4f56-8c3a-4d3df14040e9)


## More Information:
Postman documentation [here](https://documenter.getpostman.com/view/16000387/2sA2xb7veq).

Front-End [here.](http://177.22.91.106:3001/)


---
⭐️ From [DarlanNoetzold](https://github.com/DarlanNoetzold)
