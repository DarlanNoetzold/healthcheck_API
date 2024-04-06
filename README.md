# HealthCheck Analytics Platform

This repository is subdivided into directories with separate modules from the same project. Below you will find an overview of each module, remembering that it is just a summary.

## Overview

The HealthCheck Analytics Platform is a comprehensive solution for monitoring and analyzing API health metrics. This project employs a microservices architecture to collect, analyze, and predict health metrics, enabling early identification of issues and performance optimization.

The system comprises several key components:

- **HealthCheck Collector API**: Extracts metrics from another API every 15 minutes, storing the results in a PostgreSQL database. A procedure is then executed to update an `is_alert` flag, analyzing whether the record falls outside the expected norm. Total amount of metrics collected:
  ![image](https://github.com/DarlanNoetzold/healthcheck_API/assets/41628589/5b9219c2-91a6-45fd-975c-4ddcaf8fe44f)

  
- **HealthCheck Dashboard**: A React frontend application that displays metrics, prediction results, and allows user interaction for data forecasting.

- **HealthCheck-gate**: A Spring API that acts as an intermediary, processing data, saving records and accuracies in the database, and communicating with the prediction API.

- **Cython Impl**: Implementation that adapted the python code by separating some bottlenecks and transforming these snippets of python code into Cython, to gain performance improvements.

  - **Data Processing Scripts**: Includes `extract_dataset_from_database`, optimized with Cython for improved performance, and `data_preparation` using Cnumpy and Cython for data preparation.

  - **Hyperparameter Tuning and Model Training**: Uses GridSearchCV or RandomizedSearchCV to explore parameter combinations, training regression and classification models for each collected metric.
  
  - **Model training - First Layer**: Responsible for training the Regression models that will predict the values ​​of the metrics.
  
  - **Model training - Second Layer**: Responsible for training the Classification models that will predict the values ​​of the is_alert flags.

- **Prediction API (`predict_next_value`)**: Allows for the prediction of metric values and the `is_alert` flag for the next 15 minutes based on the provided data.

- **Utils**: Auxiliary Scripts:
  - **start all.sh**: Script para iniciar a aplicação completa, as suas dependências e seus módulos.

  - **procudure_to_update_is_alert_flag.sql**: Procedure utilizada para atualizar a flag is_alert.

  - **crontab**: Modelo do Crontab usado para execução do treinamento dos modelos todo dia (uma vez cada camada).
  
  - **Dockerfile**: Dockerfile em desenvolvimento.  

- **Python Impl**: DEPRECATED

## Deployment

The installation process will be detailed in the future, using docker to facilitate installation or with a manual setup, using the [start.sh](https://github.com/DarlanNoetzold/healthcheck_API/blob/main/start_all.sh) file.

## Best Results

Training Best results on local architecture:
https://github.com/DarlanNoetzold/healthcheck_API/blob/main/model_accuracies.csv

Training Best results on Google Colab Pro:
https://github.com/DarlanNoetzold/healthcheck_API/blob/main/model_accuracies_Google_pro.csv

### Graphics:

![image](https://github.com/DarlanNoetzold/healthcheck_API/assets/41628589/1862af51-27a4-4727-9b99-79ea45405e6d)

![image](https://github.com/DarlanNoetzold/healthcheck_API/assets/41628589/322edd39-0ac8-4f56-8c3a-4d3df14040e9)


## More Information:
Postman documentation [here](https://documenter.getpostman.com/view/16000387/2sA2xb7veq).

Front-End [here.](http://177.22.91.106:3001/)


## Google Colab Pro training dash:
https://colab.research.google.com/drive/15aHlN6EIzTdeBKjvkjyOW_kpJ_ydGNwV?usp=sharing

---
⭐️ From [DarlanNoetzold](https://github.com/DarlanNoetzold)
