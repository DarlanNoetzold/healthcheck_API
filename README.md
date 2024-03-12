# HealthCheck Analytics Platform

## Visão Geral

A HealthCheck Analytics Platform é uma solução abrangente para monitoramento e análise de métricas de saúde de APIs. Este projeto utiliza uma arquitetura de microserviços para coletar, analisar e prever métricas de saúde, permitindo a identificação precoce de problemas e a otimização do desempenho.

O sistema é composto por vários componentes principais:

- **API de HealthCheck Collector**: Extrai métricas de outra API a cada 15 minutos, armazenando os resultados em um banco de dados PostgreSQL. Uma procedure é então executada para atualizar uma flag `is_alert`, analisando se o registro está fora do padrão esperado.

- **Scripts de Processamento de Dados**: Inclui `extract_dataset_from_database`, otimizado com Cython para melhorar a performance, e `data_preparation` usando Cnumpy e Cython para preparação de dados.

- **Tuning de Hiperparâmetros e Treinamento de Modelos**: Utiliza GridSearchCV ou RandomizedSearchCV para explorar combinações de parâmetros, treinando modelos de regressão e classificação para cada métrica coletada.

- **API de Previsão (`predict_next_value`)**: Permite a previsão do valor das métricas e da flag `is_alert` para os próximos 15 minutos com base nos dados fornecidos.

- **HealthCheck-gate**: Uma API Spring que atua como intermediária, tratando dados, salvando registros e acurácias no banco e comunicando-se com a API de previsão.

- **HealthCheck Dashboard**: Uma aplicação frontend em React que exibe as métricas, os resultados das previsões e permite a interação do usuário para a previsão de dados.

## Implantação

Processo de instação será detalhado futuramente, usando docker para facilitar a instalação ou com uma instalação manual, usando o arquivo [start.sh](https://github.com/DarlanNoetzold/healthcheck_API/blob/main/start_all.sh).

## Fluxograma:

![image](https://github.com/DarlanNoetzold/healthcheck_API/assets/41628589/f32aa5bc-a1a2-411f-862a-bd3e9086aa52)

Documentação do Postman [aqui](https://documenter.getpostman.com/view/16000387/2sA2xb7veq).

Front-End [aqui.](http://177.22.91.106:3001/)

---
⭐️ From [DarlanNoetzold](https://github.com/DarlanNoetzold)
