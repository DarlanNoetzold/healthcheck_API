import React, { useEffect, useState } from 'react';
import { Table, Button, Modal, Form, Input } from 'antd';
import moment from 'moment';
import { saveAs } from 'file-saver';
import { ApiService } from '../api/ApiService';

function ModelAccuracies() {
  const [modelAccuracies, setModelAccuracies] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [currentAccuracy, setCurrentAccuracy] = useState(null);

  useEffect(() => {
    fetchModelAccuracies();
  }, []);

  const fetchModelAccuracies = () => {
    ApiService.fetchModelAccuracies().then((response) => {
      setModelAccuracies(response.data);
    });
  };

  const handleEdit = (record) => {
    setCurrentAccuracy(record);
    setIsModalOpen(true);
  };

  const handleDelete = (id) => {
    ApiService.deleteModelAccuracy(id).then(fetchModelAccuracies);
  };

  const handleOk = () => {
    setIsModalOpen(false);
    if (currentAccuracy) {
      ApiService.updateModelAccuracy(currentAccuracy.id, currentAccuracy).then(fetchModelAccuracies);
    }
    setCurrentAccuracy(null);
  };

  const handleCancel = () => {
    setIsModalOpen(false);
    setCurrentAccuracy(null);
  };

  const handleChange = (e) => {
    setCurrentAccuracy({ ...currentAccuracy, [e.target.name]: e.target.value });
  };

  const downloadCSV = () => {
    const filename = "model_accuracies.csv";
    let csvContent = "data:text/csv;charset=utf-8,";
    csvContent += "Model Name,Accuracy Name,Metric Name,Accuracy Value,Training Date\n";

    modelAccuracies.forEach((accuracy) => {
      const row = [
        accuracy.modelName,
        accuracy.accuracyName,
        accuracy.metricName,
        `${parseFloat(accuracy.accuracyValue).toFixed(2)}%`,
        moment(accuracy.trainingDate).format('YYYY-MM-DD')
      ].join(",");
      csvContent += row + "\n";
    });

    const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8" });
    saveAs(blob, filename);
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

  return (
    <div>
      <h2>Model Accuracies</h2>
      <Button type="primary" onClick={() => { setIsModalOpen(true); setCurrentAccuracy(null); }}>
        Add New Accuracy
      </Button>
      <Button style={{ margin: '0 8px' }} onClick={downloadCSV}>
        Download CSV
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
