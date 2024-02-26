import React, { useEffect, useState } from 'react';
import { ApiService } from './ApiService';

function Metrics() {
  const [metrics, setMetrics] = useState([]);

  useEffect(() => {
    ApiService.fetchMetrics().then((response) => setMetrics(response.data));
  }, []);

  return (
    <div>
      <h2>Metrics</h2>
      <ul>
        {metrics.map((metric) => (
          <li key={metric.id}>{metric.name}: {metric.valueType}</li>
        ))}
      </ul>
    </div>
  );
}

export default Metrics;
