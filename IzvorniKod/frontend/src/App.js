import React from 'react';
import './App.css';
import { Switch, Route} from 'react-router-dom';
import Header from './components/header/header.component'
import Homepage from './pages/homepage/homepage.component'
import AuthPage from './pages/authPage/authPage.component'
import GymList from './pages/gymList/gymList.component'

function App() {
  return (
    <div className="App">
        <Header />
        <div className = 'page-container'>
          <Switch>
            <Route exact path="/" component={Homepage} />
            <Route exact path="/auth" component={AuthPage} />
            <Route exact path="/gymList" component={GymList} />
          </Switch>
        </div>
    </div>
  );
}

export default App;
