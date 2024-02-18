CREATE OR REPLACE PROCEDURE update_is_alert_flag()
LANGUAGE plpgsql
AS $$
DECLARE
    var_name TEXT;
    var_avg_val DOUBLE PRECISION;
    var_stddev_val DOUBLE PRECISION;
    var_threshold DOUBLE PRECISION;
BEGIN
    UPDATE measurement m
    SET is_alert = FALSE;
    
    FOR var_name, var_avg_val, var_stddev_val IN
        SELECT 
            mr.name,
            AVG(m.value) AS avg_val, 
            STDDEV(m.value) AS stddev_val
        FROM measurement m
        JOIN metric_response mr ON m.metric_response_id = mr.id
        GROUP BY mr.name
    LOOP
        var_threshold := var_avg_val + var_stddev_val;

        UPDATE measurement m
        SET is_alert = TRUE
        FROM metric_response mr
        WHERE m.metric_response_id = mr.id
        AND mr.name = var_name
        AND m.value > var_threshold;
    END LOOP;
END;
$$;
