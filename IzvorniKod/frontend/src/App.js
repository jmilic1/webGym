import React from 'react';
import './App.css';
import { Switch, Route} from 'react-router-dom';
import Header from './components/header/header.component'
import Homepage from './pages/homepage/homepage.component'
import AuthPage from './pages/authPage/authPage.component'
import GymList from './pages/gymList/gymList.component'
import Logout from './components/logout/logout.component'
import CoachPlans from "./pages/coachPlans/coachPlans.component";
import Footer from "./components/footer/footer.component";
import OwnerGyms from "./pages/ownerGyms/ownerGyms.component";
class App extends React.Component {

  constructor(){
    super()
    this.state = {
      backendURL: "https://web-gym2.herokuapp.com/",
      //backendURL: "http://localhost:8080/",
      //backendURL: "https://f74a7152-35cc-4315-878e-7202dfe1b74c.mock.pstmn.io/",
      loggedIn: false,
      username: '',
      name: "",
      surname: "",
      role: "",
      // coach: false,
      // admin: false,
      // gymOwner: false
    }
  }

  handleLogin = (username, name, surname, role) => {//client, coach, admin, gymOwner) => {
    console.log("Uloga za props: " + role)
    this.setState({
      name:name,
      surname: surname,
      role: role,
      // client: client,
      // coach: coach,
      // admin:admin,
      // gymOwner: gymOwner,
      username: username,
      loggedIn: true
    })
  }

  handleLogout = () => {
    this.setState({
      name:'',
      surname: '',
      // coach: false,
      // admin:false,
      // gymOwner: false,
      // client: false,
      role: "",
      username: false,
      loggedIn: false
    })
  }

  render(){
    return (
      <div className="App">
          <Header  loggedIn = {this.state.loggedIn} role = {this.state.role}/>
          <div className = 'page-container'>
            <Switch>
              <Route exact path="/" component={Homepage} />
              <Route exact path="/auth" render={(props) => <AuthPage {...props} handleLogin={this.handleLogin} backendURL = {this.state.backendURL} />} />
              <Route exact path="/logout" render={(props) => <Logout {...props} handleLogout={this.handleLogout} backendURL = {this.state.backendURL}/>} />
              <Route exact path="/gymList" render={(props) => <GymList {...props} backendURL = {this.state.backendURL} />} />
              <Route exact path="/myPlans" render={(props) => <CoachPlans {...props} backendURL = {this.state.backendURL}/>} />
              <Route exact path="/myGyms" render={(props) => <OwnerGyms {...props} backendURL = {this.state.backendURL}/>} />
            </Switch>
          </div>
          <Footer/>
      </div>
    )
  }
}

export default App;

