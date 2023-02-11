import {BrowserRouter, Routes, Route, Link, NavLink} from 'react-router-dom'

//pages
import Home from './pages/Home'
import Transactions from './pages/Transactions'

function App() {
  return (
    <BrowserRouter>
      <NavLink to="/">Главное</NavLink>
      <NavLink to="transactions">Транзакции</NavLink>
      <main>
        <Routes>
          <Route index element={<Home />}/>
          <Route path="transactions" element={<Transactions />}/>
        </Routes>
      </main>
    </BrowserRouter>
  );
}

export default App;
