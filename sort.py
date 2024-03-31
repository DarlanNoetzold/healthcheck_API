import pandas as pd

# Carregando o arquivo
file_path = 'model_accuracies.csv'
df = pd.read_csv(file_path)

# Ordenando os dados
df_sorted = df.sort_values(by=['Metric Name', 'Model Name'])

# Exportando para um novo arquivo
sorted_file_path = 'sorted_model_accuracies.csv'
df_sorted.to_csv(sorted_file_path, index=False)

