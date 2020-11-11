import React from 'react';
import './App.css';
import { Switch, Route} from 'react-router-dom';
import Header from './components/header/header.component'
import Homepage from './pages/homepage/homepage.component'
import AuthPage from './pages/authPage/authPage.component'
import GymList from './pages/gymList/gymList.component'
import Logout from './components/logout/logout.component'

class App extends React.Component {

  constructor(){
    super()
    this.state = {
      backendURL: "https://web-gym2.herokuapp.com/",
      //backendURL: "http://localhost:8080/",
      loggedIn: false,
      username: '',
      name: "",
      surname: "",
      coach: false,
      admin: false,
      gymOwner: false
    }
  }

  handleLogin = (username, name, surname, client, coach, admin, gymOwner) => {
    this.setState({
      name:name,
      surname: surname,
      client: client,
      coach: coach,
      admin:admin,
      gymOwner: gymOwner,
      username: username,
      loggedIn: true
    })
  }

  handleLogout = () => {
    this.setState({
      name:'',
      surname: '',
      coach: '',
      admin:false,
      gymOwner: false,
      client: false,
      username: false,
      loggedIn: false
    })
  }

  render(){
    return (
      <div className="App">
          <Header loggedIn = {this.state.loggedIn}/>
          <div className = 'page-container'>
            <Switch>
              <Route exact path="/" component={Homepage} />
              <Route exact path="/auth" render={(props) => <AuthPage {...props} handleLogin={this.handleLogin} backendURL = {this.state.backendURL} />} />
              <Route exact path="/logout" render={(props) => <Logout {...props} handleLogout={this.handleLogout} backendURL = {this.state.backendURL}/>} />
              <Route exact path="/gymList" component={GymList} backendURL = {this.state.backendURL}/>
            </Switch>
          </div>
      </div>
    )
  }
}

export default App;

