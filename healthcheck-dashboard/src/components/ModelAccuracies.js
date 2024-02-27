import React, { useEffect, useState } from 'react';
import { Table, Button, Modal, Form, Input } from 'antd';
import moment from 'moment';
import { ApiService } from '../api/ApiService';

function ModelAccuracies() {
  const [modelAccuracies, setModelAccuracies] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [currentAccuracy, setCurrentAccuracy] = useState(null);

  const fetchModelAccuracies = () => {
    ApiService.fetchModelAccuracies().then((response) => {
      setModelAccuracies(response.data);
    });
  };

  useEffect(() => {
    fetchModelAccuracies();
  }, []);

  const handleEdit = (record) => {
    setCurrentAccuracy(record);
    setIsModalOpen(true);
  };

  const handleDelete = (id) => {
    ApiService.deleteModelAccuracy(id).then(() => {
      fetchModelAccuracies();
    });
  };

  const handleOk = () => {
    setIsModalOpen(false);
    if (currentAccuracy) {
      ApiService.updateModelAccuracy(currentAccuracy.id, currentAccuracy).then(() => {
        setCurrentAccuracy(null);
        fetchModelAccuracies();
      });
    } 
  };

  const handleCancel = () => {
    setIsModalOpen(false);
    setCurrentAccuracy(null);
  };

  const columns = [
    {
      title: 'Model Name',
      dataIndex: 'modelName',
      key: 'modelName',
    },
    {
      title: 'Accuracy Name',
      dataIndex: 'accuracyName',
      key: 'accuracyName',
    },
    {
      title: 'Metric Name',
      dataIndex: 'metricName',
      key: 'metricName',
    },
    {
      title: 'Accuracy Value',
      dataIndex: 'accuracyValue',
      key: 'accuracyValue',
      render: (text) => `${parseFloat(text).toFixed(2)}%`,
    },
    {
      title: 'Training Date',
      dataIndex: 'trainingDate',
      key: 'trainingDate',
      render: (text) => moment(text).format('YYYY-MM-DD'),
    },
    {
      title: 'Action',
      key: 'action',
      render: (_, record) => (
        <>
          <Button onClick={() => handleEdit(record)}>Edit</Button>
          <Button onClick={() => handleDelete(record.id)} danger>Delete</Button>
        </>
      ),
    },
  ];

  const handleChange = (e) => {
    setCurrentAccuracy({ ...currentAccuracy, [e.target.name]: e.target.value });
  };

  return (
    <div>
      <h2>Model Accuracies</h2>
      <Button type="primary" onClick={() => { setIsModalOpen(true); setCurrentAccuracy(null); }}>
        Add New Accuracy
      </Button>
      <Table columns={columns} dataSource={modelAccuracies} rowKey="id" />
      <Modal title={currentAccuracy ? "Edit Model Accuracy" : "Add Model Accuracy"} open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>
        <Form>
          <Form.Item label="Model Name">
            <Input name="modelName" value={currentAccuracy?.modelName} onChange={handleChange} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}

export default ModelAccuracies;
