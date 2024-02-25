import React from 'react';
import './App.css';
import Records from './components/Records';
import Metrics from './components/Metrics';
import ModelAccuracies from './components/ModelAccuracies';

function App() {
  return (
    <div className="App">
      <h1>Dashboard</h1>
      <Records />
      <Metrics />
      <ModelAccuracies />
    </div>
  );
}

export default App;
