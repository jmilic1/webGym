import React from 'react'
import './authPage.styles.scss'
import CustomButton from '../../components/custom-buttom/custom-button.component'
import FormInput from '../../components/formInput/formInput.component'

class AuthPage extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      usernameLogin: '',
      passwordLogin: '',
      usernameRegistration: '',
      passwordRegistration: '',
      name:"",
      surname: "",
      email: "",
      role: "CLIENT",
      selectedOption: "client"

    }
  }

  handleChange = event => {
    const { value, name } = event.target;

    this.setState({ [name]: value });
  };



  handleLogin = async (username, name, surname, role, email, payPalAccount, height, weight) => {
      await this.props.handleLogin(username, name, surname, role, email, payPalAccount, height, weight)
      this.props.history.push("/")
  }


  handleButtonLoginClick = () => {
    fetch(this.props.backendURL + "login", {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({username:this.state.usernameLogin, password:this.state.passwordLogin})
    }).then(response => {
      if(response.status === 200){
        return response.json()
      } else {
        return Promise.reject()
      }
    }).then(res => {
      console.log(res)
      this.handleLogin(this.state.usernameLogin, res.name, res.surname, res.role, res.email, res.payPalAccount, res.height, res.weight) //res.coach, res.admin, res.gymOwner)
    }, function(){
      alert("Krivo korisnicko ime ili lozinka")
    })
  }

  handleButtonRegisterClick = () => {
    fetch(this.props.backendURL + "registration", {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({username:this.state.usernameRegistration, password:this.state.passwordRegistration, name:this.state.name, surname: this.state.surname,
                                  email:this.state.email, role: this.state.role}) //client: this.state.client, coach: this.state.coach, gymOwner: this.state.gymOwner})
    }).then(response => {
      if(response.status === 200){
        this.handleLogin(this.state.usernameRegistration, this.state.name, this.state.surname, this.state.role) //this.state.client, this.state.coach, false, this.state.gymOwner)
        return Promise.reject()
      } else{
        return response.json()
      }
    }).then(err => {
      alert(err.reason)
    }, function(){
      console.log("Prijava uspjesna");
    })
  }

  handleRoleChange = changeEvent => {
    this.setState({
      selectedOption: changeEvent.target.value,
      role: changeEvent.target.value.toUpperCase(),
      // client: changeEvent.target.value === "client",
      // coach: changeEvent.target.value === "coach",
      // gymOwner: changeEvent.target.value === "gymOwner"
    });
  };


  render() {
    return (
      <div className='auth-page'>
        <fieldset>
          <legend>Prijava</legend>
          <div className='login-container'>
            <FormInput
                name='usernameLogin'
                type='username'
                handleChange={this.handleChange}
                value={this.state.usernameLogin}
                label='Korisničko ime'
                required
            />
            <div className='padding-bottom'>
              <FormInput
                  name='passwordLogin'
                  type='password'
                  value={this.state.passwordLogin}
                  handleChange={this.handleChange}
                  label='Lozinka'
                  required
              />
            </div>
            <CustomButton onClick = {this.handleButtonLoginClick} > Prijava </CustomButton>
          </div>
        </fieldset>

        <fieldset>
          <legend>Registracija</legend>
          <div className='registration-container'>
            <div className = 'registration-name-and-surname'>
              <FormInput
                  name='name'
                  type='name'
                  handleChange={this.handleChange}
                  value={this.state.name}
                  label='Ime'
                  required
                  style = {{"width": "130px"}}
              />
              <FormInput
                  name='surname'
                  type='surname'
                  handleChange={this.handleChange}
                  value={this.state.surname}
                  label='Prezime'
                  required
                  style = {{"width": "130px"}}
              />
            </div>
            <FormInput
                name='email'
                type='email'
                handleChange={this.handleChange}
                value={this.state.email}
                label='E-posta'
                required
            />
            <div className= 'username-and-password-registration'>
              <FormInput
                  name='usernameRegistration'
                  type='username'
                  handleChange={this.handleChange}
                  value={this.state.usernameRegistration}
                  label='Korisničko ime'
                  required
              />
              <FormInput
                  name='passwordRegistration'
                  type='password'
                  value={this.state.passwordRegistration}
                  handleChange={this.handleChange}
                  label='Lozinka'
                  required
              />
            </div>
            <div className='role-input-container'>
              <div>
                <input type='radio' value='client'  checked={this.state.selectedOption === "client"} onChange={this.handleRoleChange}/>
                <label>Klijent</label>
              </div>
              <div>
                <input type='radio' value='coach' checked={this.state.selectedOption === "coach"} onChange={this.handleRoleChange}/>
                <label>Trener</label>
              </div>
              <div>
                <input type='radio' value='owner' checked={this.state.selectedOption === "owner"} onChange={this.handleRoleChange}/>
                <label>Voditelj teretane</label>
              </div>
            </div>
            <CustomButton onClick = {this.handleButtonRegisterClick}> Registracija </CustomButton>
            <p>{this.state.error}</p>
          </div>
        </fieldset>
      </div>
    );
  }
}

export default AuthPage
