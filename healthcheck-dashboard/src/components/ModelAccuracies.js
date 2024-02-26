import React, { useState, useEffect } from 'react';
import { ApiService } from '../api/ApiService';

function ModelAccuracies() {
  const [modelAccuracies, setModelAccuracies] = useState([]);

  useEffect(() => {
    fetchModelAccuracies();
  }, []);

  const fetchModelAccuracies = () => {
    ApiService.fetchModelAccuracies().then((response) => {
      setModelAccuracies(response.data);
    });
  };

  return (
    <div>
      <h2>Model Accuracies</h2>
      <table>
        <thead>
          <tr>
            <th>Model Name</th>
            <th>Accuracy Name</th>
            <th>Metric Name</th>
            <th>Accuracy Value</th>
            <th>Training Date</th>
          </tr>
        </thead>
        <tbody>
          {modelAccuracies.map((accuracy) => (
            <tr key={accuracy.id}>
              <td>{accuracy.modelName}</td>
              <td>{accuracy.accuracyName}</td>
              <td>{accuracy.metricName}</td>
              <td>{accuracy.accuracyValue.toFixed(2)}</td>
              <td>{new Date(accuracy.trainingDate).toLocaleDateString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default ModelAccuracies;
