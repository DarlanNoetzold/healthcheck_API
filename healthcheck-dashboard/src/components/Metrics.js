import React, { useEffect, useState } from 'react';
import { Table } from 'antd';
import { ApiService } from '../api/ApiService';

function Metrics() {
  const [metrics, setMetrics] = useState([]);

  useEffect(() => {
    ApiService.fetchMetrics().then((response) => setMetrics(response.data));
  }, []);

  const columns = [
    {
      title: 'Metric Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Value Type',
      dataIndex: 'valueType',
      key: 'valueType',
      align: 'right',
    },
  ];

  return <Table columns={columns} dataSource={metrics} rowKey="id" />;
}

export default Metrics;
