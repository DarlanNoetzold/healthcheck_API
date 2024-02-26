import React, { useEffect, useState } from 'react';
import { ApiService } from './ApiService';

function Records() {
  const [records, setRecords] = useState([]);

  useEffect(() => {
    ApiService.fetchRecords().then((response) => setRecords(response.data));
  }, []);

  return (
    <div>
      <h2>Records</h2>
      <ul>
        {records.map((record) => (
          <li key={record.id}>{record.propertyName}: {record.predictValue}</li>
        ))}
      </ul>
    </div>
  );
}

export default Records;
