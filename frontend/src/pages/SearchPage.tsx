import { useState } from 'react';
import {
  TextInput, Select, SelectItem, DatePicker, DatePickerInput,
  Button, DataTable, TableContainer, Table, TableHead, TableRow,
  TableHeader, TableBody, TableCell, Tag,
} from '@carbon/react';
import { Search } from '@carbon/react/icons';
import PageLayout from './PageLayout';
import './PageLayout.css';

const STATUS_OPTIONS = [
  { value: '', label: '' },
  { value: 'DFT', label: 'Draft' },
  { value: 'SUB', label: 'Submitted' },
  { value: 'APP', label: 'Approved' },
  { value: 'REJ', label: 'Rejected' },
  { value: 'EXP', label: 'Expired' },
];

const DATE_TYPE_OPTIONS = [
  { value: 'INITIATION', label: 'Initiation' },
  { value: 'DECISION',   label: 'Decision' },
  { value: 'EFFECTIVE',  label: 'Effective' },
  { value: 'EXPIRY',     label: 'Expiry' },
  { value: 'SUBMITTED',  label: 'Submitted' },
];

const APPROVAL_OPTIONS = [
  { value: '',  label: '' },
  { value: 'Y', label: 'Yes' },
  { value: 'N', label: 'No' },
];

interface SearchResult {
  id: string;
  amendNo: string;
  name: string;
  amendName: string;
  status: string;
  orgUnit: string;
  effectiveDate: string;
  expiryDate: string;
  holder: string;
}

const MOCK_RESULTS: SearchResult[] = [
  { id: '10001', amendNo: '0', name: 'Okanagan FSP 2023', amendName: '—', status: 'APP', orgUnit: 'DPG', effectiveDate: '2023-04-01', expiryDate: '2028-03-31', holder: 'Tolko Industries' },
  { id: '10002', amendNo: '1', name: 'Skeena FSP 2022',   amendName: 'Boundary Amend', status: 'SUB', orgUnit: 'DSE', effectiveDate: '2022-06-15', expiryDate: '2027-06-14', holder: 'Canfor Corporation' },
  { id: '10003', amendNo: '0', name: 'Cariboo FSP 2021',  amendName: '—', status: 'DFT', orgUnit: 'DQU', effectiveDate: '—', expiryDate: '—', holder: 'West Fraser Mills' },
];

const HEADERS = [
  { key: 'id',            header: 'FSP ID' },
  { key: 'amendNo',       header: 'Amnd #' },
  { key: 'name',          header: 'FSP Name' },
  { key: 'amendName',     header: 'Amendment Name' },
  { key: 'status',        header: 'Status' },
  { key: 'orgUnit',       header: 'Organization Unit' },
  { key: 'effectiveDate', header: 'Effective Date' },
  { key: 'expiryDate',    header: 'Expiry Date' },
  { key: 'holder',        header: 'Agreement Holder' },
];

const STATUS_TAG: Record<string, string> = { APP: 'green', SUB: 'blue', DFT: 'gray', REJ: 'red', EXP: 'warm-gray' };

interface SearchForm {
  orgUnit: string;
  fspId: string;
  status: string;
  fspName: string;
  amendName: string;
  dateType: string;
  holder: string;
  approval: string;
}

const EMPTY_FORM: SearchForm = { orgUnit: '', fspId: '', status: '', fspName: '', amendName: '', dateType: 'INITIATION', holder: '', approval: '' };

export default function SearchPage() {
  const [form, setForm]       = useState<SearchForm>(EMPTY_FORM);
  const [results, setResults] = useState<SearchResult[] | null>(null);

  const set = <K extends keyof SearchForm>(k: K, v: SearchForm[K]) => setForm(f => ({ ...f, [k]: v }));

  const handleSearch = () => setResults(MOCK_RESULTS);
  const handleClear  = () => { setForm(EMPTY_FORM); setResults(null); };

  return (
    <PageLayout screenId="FSP100" title="Search">
      {/* Search form */}
      <div className="form-section">
        <div className="search-grid">
          <Select id="orgUnit" labelText="Organization Unit" value={form.orgUnit} onChange={e => set('orgUnit', e.target.value)}>
            <SelectItem value="" text="" />
            <SelectItem value="DPG" text="DPG – Penticton" />
            <SelectItem value="DSE" text="DSE – Skeena" />
            <SelectItem value="DQU" text="DQU – Quesnel" />
          </Select>

          <TextInput id="fspId" labelText="FSP ID" value={form.fspId} onChange={e => set('fspId', e.target.value)} maxLength={10} />

          <Select id="status" labelText="Status" value={form.status} onChange={e => set('status', e.target.value)}>
            {STATUS_OPTIONS.map(o => <SelectItem key={o.value} value={o.value} text={o.label} />)}
          </Select>

          <TextInput id="fspName" labelText="FSP Name" value={form.fspName} onChange={e => set('fspName', e.target.value)} maxLength={120} />

          <TextInput id="amendName" labelText="Amendment Name" value={form.amendName} onChange={e => set('amendName', e.target.value)} maxLength={30} />

          <Select id="dateType" labelText="Date Type" value={form.dateType} onChange={e => set('dateType', e.target.value)}>
            {DATE_TYPE_OPTIONS.map(o => <SelectItem key={o.value} value={o.value} text={o.label} />)}
          </Select>

          <DatePicker datePickerType="range" dateFormat="Y-m-d">
            <DatePickerInput id="dateFrom" labelText="Date From (YYYY-MM-DD)" placeholder="YYYY-MM-DD" />
            <DatePickerInput id="dateTo"   labelText="Date To (YYYY-MM-DD)"   placeholder="YYYY-MM-DD" />
          </DatePicker>

          <TextInput id="holder" labelText="Agreement Holder" value={form.holder} onChange={e => set('holder', e.target.value)} maxLength={8} />

          <Select id="approval" labelText="Approval Required" value={form.approval} onChange={e => set('approval', e.target.value)}>
            {APPROVAL_OPTIONS.map(o => <SelectItem key={o.value} value={o.value} text={o.label} />)}
          </Select>
        </div>

        <div className="form-actions">
          <Button kind="primary"   renderIcon={Search} onClick={handleSearch}>Search</Button>
          <Button kind="secondary" onClick={handleClear}>Clear</Button>
        </div>
      </div>

      {/* Results */}
      {results !== null && (
        <>
          <p className="results-count"><strong>{results.length}</strong> rows returned</p>
          <DataTable rows={results} headers={HEADERS}>
            {({ rows, headers, getTableProps, getHeaderProps, getRowProps }) => (
              <TableContainer>
                <Table {...getTableProps()} size="sm">
                  <TableHead>
                    <TableRow>
                      {headers.map(h => <TableHeader {...getHeaderProps({ header: h })} key={h.key}>{h.header}</TableHeader>)}
                    </TableRow>
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

      <style>{`
        .search-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 1rem 1.5rem; }
      `}</style>
    </PageLayout>
  );
}
