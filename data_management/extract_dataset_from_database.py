import pandas as pd
import os
import psycopg2


def extract():
    # Informações de conexão com o banco de dados
    db_config = {
        "host": "192.168.18.75",
        "port": "5432",
        "user": "postgres",
        "password": "postgres",
        "dbname": "healthcheck"
    }

    # Lista de nomes de métricas
    metric_names = [
        "disk.free", "hikaricp.connections.acquire",
        "http.server.requests", "http.server.requests.active",
        "jvm.buffer.count", "jvm.buffer.memory.used", "jvm.buffer.total.capacity",
        "jvm.classes.loaded", "jvm.compilation.time", "jvm.gc.memory.allocated",
        "jvm.gc.memory.promoted", "jvm.gc.overhead", "jvm.gc.pause",
        "jvm.memory.committed", "jvm.memory.usage.after.gc",
        "jvm.memory.used", "jvm.threads.daemon", "jvm.threads.live", "jvm.threads.peak",
        "jvm.threads.started", "jvm.threads.states", "logback.events", "process.cpu.usage",
        "process.uptime", "system.cpu.usage", "system.load.average.1m"
    ]

    output_dir = "metrics_from_database"

    # Conecta ao banco de dados
    conn = psycopg2.connect(**db_config)

    for metric_name in metric_names:
        query = f"""
            SELECT 
                mr.name AS metric_name,
                mr.description,
                mr.base_unit,
                m.statistic,
                m.value AS measurement_value,
                m.is_alert,
                t.tag,
                tv.value AS tag_value
            FROM 
                metric_response mr
            LEFT JOIN 
                measurement m ON mr.id = m.metric_response_id
            LEFT JOIN 
                tag t ON mr.id = t.metric_response_id
            LEFT JOIN 
                tag_values tv ON t.id = tv.tag_id
            WHERE 
                mr.name = '{metric_name}'
            ORDER BY 
                mr.name, m.id, t.id, tv.tag_id;
        """

        df = pd.read_sql_query(query, conn)

        # Gera o arquivo CSV para a métrica atual no diretório especificado
        filename = os.path.join(output_dir, f"{metric_name.replace('.', '_')}.csv")
        df.to_csv(filename, index=False)
        print(f"Arquivo gerado: {filename}")

    # Fecha a conexão com o banco de dados
    conn.close()



if __name__ == "__main__":
    extract()
