import pandas as pd
import psycopg2
import os

def extract():
    db_config = {
        'host': '192.168.18.75',
        'port': '5432',
        'user': 'postgres',
        'password': 'postgres',
        'dbname': 'healthcheck'
    }

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

    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    conn = psycopg2.connect(**db_config)

    for metric_name in metric_names:
        query = """
        SELECT mr.name AS metric_name, mr.description, mr.base_unit,
        m.statistic, m.value AS measurement_value, t.tag, tv.value AS tag_value, m.is_alert AS is_alert
        FROM metric_response mr
        LEFT JOIN measurement m ON mr.id = m.metric_response_id
        LEFT JOIN tag t ON mr.id = t.metric_response_id
        LEFT JOIN tag_values tv ON t.id = tv.tag_id
        WHERE mr.name = %s
        ORDER BY mr.name, m.id, t.id, tv.tag_id;
        """
        
        df = pd.read_sql_query(query, conn, params=[metric_name])
        
        filename = os.path.join(output_dir, f"{metric_name.replace('.', '_')}.csv")
        df.to_csv(filename, index=False)
        print(f"Arquivo gerado: {filename}")

    conn.close()