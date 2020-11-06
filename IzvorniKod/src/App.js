import React from 'react';
import './App.css';
import { Switch, Route} from 'react-router-dom';
import Header from './components/header/header.component'
import Homepage from './pages/homepage/homepage.component'
import AuthPage from './pages/authPage/authPage.component'
import GymList from './pages/gymList/gymList.component'

class App extends React.Component {

  constructor(){
    super()
    this.state = {
      name: "",
      surname: "",
      coach: false,
      admin: false,
      gymOwner: false
    }
  }

  handleLogin = (name, surname, coach, admin, gymOwner) => {
    this.setState({
      name:name,
      surname: surname,
      coach: coach,
      admin:admin,
      gymOwner: gymOwner
    })
  }

  render(){
    return (
      <div className="App">
          <Header />
          <div className = 'page-container'>
            <Switch>
              <Route exact path="/" component={Homepage} />
              <Route exact path="/auth" render={(props) => <AuthPage {...props} handleLogin={this.handleLogin} />} />
              <Route exact path="/gymList" component={GymList} />
            </Switch>
          </div>
      </div>
    )
  }
}

export default App;
