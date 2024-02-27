import React, { useState, useEffect } from 'react';
import { ApiService } from '../api/ApiService';
import { List, Input, Button, Form, Modal } from 'antd';
import { EditOutlined, DeleteOutlined } from '@ant-design/icons';

function Records() {
  const [records, setRecords] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [currentRecord, setCurrentRecord] = useState({ propertyName: '', predictValue: 0 });

  useEffect(() => {
    fetchRecords();
  }, []);

  const fetchRecords = () => {
    ApiService.fetchRecords().then((response) => setRecords(response.data));
  };

  const handleSubmit = () => {
    if (currentRecord.id) {
      ApiService.updateRecord(currentRecord.id, currentRecord).then(() => {
        fetchRecords();
        setIsModalVisible(false);
      });
    } else {
      ApiService.createRecord(currentRecord).then(() => {
        fetchRecords();
        setIsModalVisible(false);
      });
    }
    setCurrentRecord({ propertyName: '', predictValue: 0 });
  };

  const showModal = (record = { propertyName: '', predictValue: 0 }) => {
    setCurrentRecord(record);
    setIsModalVisible(true);
  };

  const handleDelete = (id) => {
    ApiService.deleteRecord(id).then(fetchRecords);
  };

  return (
    <div>
      <h2>Records</h2>
      <Button type="primary" onClick={() => showModal()}>
        Add New Record
      </Button>
      <List
        itemLayout="horizontal"
        dataSource={records}
        renderItem={record => (
          <List.Item
            actions={[
              <EditOutlined key="edit" onClick={() => showModal(record)} />,
              <DeleteOutlined key="delete" onClick={() => handleDelete(record.id)} />,
            ]}
          >
            <List.Item.Meta
              title={record.propertyName}
              description={`Predict Value: ${record.predictValue}`}
            />
          </List.Item>
        )}
      />
      <Modal title={currentRecord.id ? 'Edit Record' : 'Add Record'} visible={isModalVisible} onOk={handleSubmit} onCancel={() => setIsModalVisible(false)}>
        <Form layout="vertical">
          <Form.Item label="Property Name">
            <Input value={currentRecord.propertyName} onChange={(e) => setCurrentRecord({ ...currentRecord, propertyName: e.target.value })} />
          </Form.Item>
          <Form.Item label="Predict Value">
            <Input type="number" value={currentRecord.predictValue} onChange={(e) => setCurrentRecord({ ...currentRecord, predictValue: e.target.value })} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}

export default Records;
