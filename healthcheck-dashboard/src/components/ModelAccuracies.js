import React, { useEffect, useState } from 'react';
import { ApiService } from './ApiService';

function ModelAccuracies() {
  const [modelAccuracies, setModelAccuracies] = useState([]);

  useEffect(() => {
    ApiService.fetchModelAccuracies().then((response) => setModelAccuracies(response.data));
  }, []);

  return (
    <div>
      <h2>Model Accuracies</h2>
      <ul>
        {modelAccuracies.map((modelAccuracy) => (
          <li key={modelAccuracy.id}>
            {modelAccuracy.modelName} - {modelAccuracy.metricName}: {modelAccuracy.accuracyValue}%
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ModelAccuracies;
