import React, { useEffect, useState } from 'react';
import { connectWebSocket, disconnectWebSocket } from './services/websocketService';
import { LineChart, Line, BarChart, Bar, XAxis, YAxis, Tooltip, CartesianGrid } from 'recharts';

const Dashboard = () => {
  const [eventsPerMinute, setEventsPerMinute] = useState({});
  const [eventTypeCounts, setEventTypeCounts] = useState({});
  const [peakHour, setPeakHour] = useState('');
  const [topEventTypes, setTopEventTypes] = useState({});


  useEffect(() => {
    connectWebSocket((data) => {
      if (data.eventsPerMinute) setEventsPerMinute(data.eventsPerMinute);
      if (data.eventTypeCounts) setEventTypeCounts(data.eventTypeCounts);
      fetch("http://localhost:8080/api/events/peak-hour")
          .then(res => res.text())
          .then(data => setPeakHour(data));

        fetch("http://localhost:8080/api/events/top-event-types?minutes=10")
          .then(res => res.json())
          .then(data => setTopEventTypes(data));
    });

    return () => {
      disconnectWebSocket();
    };
  }, []);

  console.log("Formatted data for chart:", eventsPerMinute);
  console.log("count events:", eventTypeCounts);


  return (
      <div style={{ backgroundColor: '#4169e1', minHeight: '100vh', padding: '20px', color: '#fff' }}>
        <h1 style={{ textAlign: 'center' }}>Real-Time Analytics Dashboard</h1>

        <div style={{
          display: 'grid',
          gridTemplateColumns: '1fr 1fr',
          gap: '20px',
          marginTop: '30px'
        }}>
          {/* Left Column */}
          <div>
            <div style={cardStyle}>
              <h2>Events Per Minute</h2>
              <LineChart width={500} height={250} data={
                Object.entries(eventsPerMinute).map(([time, count]) => ({ time, count }))
              }>
                <XAxis dataKey="time" stroke="#fff" />
                <YAxis stroke="#fff" />
                <Tooltip />
                <CartesianGrid stroke="#ccc" />
                <Line type="monotone" dataKey="count" stroke="#ffcc00" />
              </LineChart>
            </div>

            <div style={cardStyle}>
              <h2>Event Type Distribution</h2>
              <BarChart width={500} height={250} data={
                Object.entries(eventTypeCounts).map(([type, count]) => ({ type, count }))
              }>
                <XAxis dataKey="type" stroke="#fff" />
                <YAxis stroke="#fff" />
                <Tooltip />
                <CartesianGrid stroke="#ccc" />
                <Bar dataKey="count" fill="#82ca9d" />
              </BarChart>
            </div>
          </div>

          {/* Right Column */}
          <div>
            <div style={{ ...cardStyle, display: 'flex', alignItems: 'center', justifyContent: 'center', height: '250px' }}>
              <div>
                <h2>Peak Hour</h2>
                <div style={{ fontSize: '48px', fontWeight: 'bold' }}>{peakHour}:00</div>
              </div>
            </div>

            <div style={cardStyle}>
              <h2>Top Event Types (Last 10 Minutes)</h2>
              <BarChart width={500} height={250} data={
                Object.entries(topEventTypes).map(([eventType, count]) => ({ eventType, count }))
              }>
                <XAxis dataKey="eventType" stroke="#fff" />
                <YAxis stroke="#fff" />
                <Tooltip />
                <CartesianGrid stroke="#ccc" />
                <Bar dataKey="count" fill="#ffc658" />
              </BarChart>
            </div>
          </div>
        </div>
      </div>
    );
  }

  const cardStyle = {
    backgroundColor: '#ffffff22',
    borderRadius: '10px',
    padding: '20px',
    marginBottom: '20px',
    boxShadow: '0 0 10px rgba(0,0,0,0.3)',
    color: '#fff'
  };

  export default Dashboard;
