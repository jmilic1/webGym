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
import UserProfile from "./pages/userProfile/userProfile.component";
import UserList from './pages/userList/userList.component'
import GymInfo from './pages/gymInfo/gymInfo.component'
import MembershipInfo from "./pages/membershipPage/membershipInfo.component";
import JobRequests from "./pages/jobRequests/jobRequests.component";
import PaymentPage from './pages/paymentPage/paymentPage.component';
import CoachInfoPage from "./pages/coachPage/coachPage.component";
import CoachClients from "./pages/coachClients/coachClients.component";
import AdminTransactions from "./pages/adminTransactions/adminTransactions.component";

class App extends React.Component {

  constructor(){
    super()
    this.state = {
      backendURL: "https://web-gym2.herokuapp.com/",
      //backendURL: "http://localhost:8080/",
      //backendURL: "https://e5b225ce-baf1-4c30-b8cb-414347908709.mock.pstmn.io/",
      loggedIn: false,
      username: "",
      name: "",
      surname: "",
      role: "",
      email: "",  
      payPalAccount: "",
      height: 0,
      weight: 0
    }
  }

  handleLogin = (username, name, surname, role, email, height, weight, payPalAccount) => {
    this.setState({
      name:name,
      surname: surname,
      role: role,
      username: username,
      loggedIn: true,
      email: email,
      payPalAccount: payPalAccount,
      height: height,
      weight: weight
    })
  }

  handleLogout = () => {
    this.setState({
      name:'',
      surname: '',
      role: "",
      username: false,
      loggedIn: false,
      email: "",
      payPalAccount: "",
      height: "",
      weight: ""
    })
  }

  render(){
    return (
      <div className="App">
          <Header loggedIn = {this.state.loggedIn} role = {this.state.role}/>
          <div className = 'page-container'>
            <Switch>
              <Route exact path="/" component={Homepage} />
              <Route exact path="/auth" render={(props) => <AuthPage {...props} handleLogin={this.handleLogin} backendURL = {this.state.backendURL} />} />
              <Route exact path="/logout" render={(props) => <Logout {...props} handleLogout={this.handleLogout} backendURL = {this.state.backendURL}/>} />
              <Route exact path="/gymList" render={(props) => <GymList {...props} backendURL={this.state.backendURL} />} />
              <Route exact path="/userList" render={(props) => <UserList {...props} backendURL = {this.state.backendURL} />} />
              <Route exact path="/myPlans" render={(props) => <CoachPlans {...props} backendURL={this.state.backendURL} />} />
              <Route exact path="/jobRequests" render={(props) => <JobRequests {...props} backendURL={this.state.backendURL} />} />
              <Route exact path="/paymentPage" render={(props) => <PaymentPage {...props} backendURL = {this.state.backendURL}/>} />
              <Route exact path="/myGyms" render={(props) => <OwnerGyms {...props} backendURL = {this.state.backendURL}/>} />
              <Route exact path="/gymInfo/:id" render={(props) => <GymInfo {...props} backendURL = {this.state.backendURL} role = {this.state.role}/>}/>
              <Route exact path="/membership/:id" render={(props) => <MembershipInfo {...props} backendURL = {this.state.backendURL} role = {this.state.role}/>}/>
              <Route exact path="/myProfile" render={(props) => <UserProfile {...props}
                username = {this.state.username} name = {this.state.name} surname = {this.state.surname} role = {this.state.role}
                backendURL = {this.state.backendURL}/>} />
              <Route exact path="/coachPage/:username" render={(props) => <CoachInfoPage {...props} backendURL = {this.state.backendURL} role = {this.state.role}
                loggedIn={this.state.loggedIn}/>}/>
              <Route exact path="/myClients" render={(props) => <CoachClients {...props} backendURL = {this.state.backendURL}/>} />
              <Route exact path="/allTransactions" render={(props) => <AdminTransactions {...props} backendURL = {this.state.backendURL}/>} />
            </Switch>
          </div>
          <Footer/>
      </div>
    )
  }
}

export default App;

