import React, { useState, useEffect } from 'react';
import { ApiService } from '../api/ApiService';

function Records() {
  const [records, setRecords] = useState([]);
  const [currentRecord, setCurrentRecord] = useState({ id: null, propertyName: '', predictValue: 0, values: [] });
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    fetchRecords();
  }, []);

  const fetchRecords = () => {
    ApiService.fetchRecords().then((response) => setRecords(response.data));
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setCurrentRecord({ ...currentRecord, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (isEditing) {
      ApiService.updateRecord(currentRecord.id, currentRecord).then(() => {
        setIsEditing(false);
        fetchRecords();
      });
    } else {
      ApiService.createRecord(currentRecord).then(fetchRecords);
    }
    setCurrentRecord({ id: null, propertyName: '', predictValue: 0, values: [] });
  };

  const editRecord = (record) => {
    setIsEditing(true);
    setCurrentRecord({ ...record });
  };

  const deleteRecord = (id) => {
    ApiService.deleteRecord(id).then(fetchRecords);
  };

  return (
    <div>
      <h2>Records</h2>
      <form onSubmit={handleSubmit}>
        <label>Property Name</label>
        <input
          type="text"
          name="propertyName"
          value={currentRecord.propertyName}
          onChange={handleInputChange}
        />
        <label>Predict Value</label>
        <input
          type="number"
          name="predictValue"
          value={currentRecord.predictValue}
          onChange={handleInputChange}
        />
        <button type="submit">{isEditing ? 'Update' : 'Add'}</button>
        {isEditing && (
          <button onClick={() => setIsEditing(false)}>Cancel Edit</button>
        )}
      </form>
      <ul>
        {records.map((record) => (
          <li key={record.id}>
            {record.propertyName}: {record.predictValue}
            <button onClick={() => editRecord(record)}>Edit</button>
            <button onClick={() => deleteRecord(record.id)}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Records;
