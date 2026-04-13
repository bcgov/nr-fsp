import React, { useState } from 'react';
import {
  TextInput, Select, SelectItem, Button,
  DataTable, TableContainer, Table, TableHead, TableRow,
  TableHeader, TableBody, TableCell, Tag,
} from '@carbon/react';
import { Search } from '@carbon/react/icons';
import PageLayout from './PageLayout';
import './PageLayout.css';

const STATUS_OPTIONS = [
  { value: '',    label: '' },
  { value: 'SUB', label: 'Submitted' },
  { value: 'OHS', label: 'Opportunity to be Heard Sent' },
  { value: 'DFT', label: 'Draft' },
];

const MOCK_RESULTS = [
  { id: '10001', amendNo: '0', extNo: '—', name: 'Okanagan FSP 2023',  status: 'SUB', submitted: '2023-03-15', submittedBy: 'jsmith',   holder: 'Tolko Industries' },
  { id: '10002', amendNo: '1', extNo: '1', name: 'Skeena FSP 2022',    status: 'OHS', submitted: '2022-11-02', submittedBy: 'bwilliams', holder: 'Canfor Corporation' },
  { id: '10003', amendNo: '0', extNo: '—', name: 'Cariboo FSP 2021',   status: 'DFT', submitted: '—',          submittedBy: '—',         holder: 'West Fraser Mills' },
];

const HEADERS = [
  { key: 'id',          header: 'FSP ID' },
  { key: 'amendNo',     header: 'Amnd #' },
  { key: 'extNo',       header: 'Ext #' },
  { key: 'name',        header: 'FSP Name' },
  { key: 'status',      header: 'Status' },
  { key: 'submitted',   header: 'Submitted' },
  { key: 'submittedBy', header: 'Submitted By' },
  { key: 'holder',      header: 'Agreement Holder' },
];

const STATUS_TAG = { SUB: 'blue', OHS: 'purple', DFT: 'gray' };

export default function InboxPage() {
  const [form, setForm]       = useState({ orgUnit: '', fspId: '', fspName: '', status: '', holder: '' });
  const [results, setResults] = useState(null);
  const set = (k, v) => setForm(f => ({ ...f, [k]: v }));

  return (
    <PageLayout screenId="FSP200" title="Inbox">
      <div className="form-section">
        <div className="search-grid">
          <Select id="orgUnit" labelText="Organization Unit" value={form.orgUnit} onChange={e => set('orgUnit', e.target.value)}>
            <SelectItem value="" text="" />
            <SelectItem value="DPG" text="DPG – Penticton" />
            <SelectItem value="DSE" text="DSE – Skeena" />
            <SelectItem value="DQU" text="DQU – Quesnel" />
          </Select>
          <TextInput id="fspId"   labelText="FSP ID"   value={form.fspId}   onChange={e => set('fspId', e.target.value)} maxLength={10} />
          <TextInput id="fspName" labelText="FSP Name" value={form.fspName} onChange={e => set('fspName', e.target.value)} maxLength={120} />
          <Select id="status" labelText="Status" value={form.status} onChange={e => set('status', e.target.value)}>
            {STATUS_OPTIONS.map(o => <SelectItem key={o.value} value={o.value} text={o.label} />)}
          </Select>
          <TextInput id="holder" labelText="Agreement Holder" value={form.holder} onChange={e => set('holder', e.target.value)} maxLength={8} />
        </div>
        <div className="form-actions">
          <Button kind="primary"   renderIcon={Search} onClick={() => setResults(MOCK_RESULTS)}>Search</Button>
          <Button kind="secondary" onClick={() => { setForm({ orgUnit: '', fspId: '', fspName: '', status: '', holder: '' }); setResults(null); }}>Clear</Button>
        </div>
      </div>

      {results !== null && (
        <>
          <p className="results-count"><strong>{results.length}</strong> rows returned</p>
          <DataTable rows={results} headers={HEADERS}>
            {({ rows, headers, getTableProps, getHeaderProps, getRowProps }) => (
              <TableContainer>
                <Table {...getTableProps()} size="sm">
                  <TableHead>
                    <TableRow>{headers.map(h => <TableHeader {...getHeaderProps({ header: h })} key={h.key}>{h.header}</TableHeader>)}</TableRow>
                  </TableHead>
                  <TableBody>
                    {rows.map(row => (
                      <TableRow {...getRowProps({ row })} key={row.id}>
                        {row.cells.map(cell => (
                          <TableCell key={cell.id}>
                            {cell.info.header === 'status'
                              ? <Tag type={STATUS_TAG[cell.value] || 'gray'} size="sm">{cell.value}</Tag>
                              : cell.value}
                          </TableCell>
                        ))}
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
            )}
          </DataTable>
        </>
      )}
      <style>{`.search-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 1rem 1.5rem; }`}</style>
    </PageLayout>
  );
}
