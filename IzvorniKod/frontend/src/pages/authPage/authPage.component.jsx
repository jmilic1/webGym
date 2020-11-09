import React from 'react'
import './authPage.styles.scss'
import CustomButton from '../../components/custom-buttom/custom-button.component'
import FormInput from '../../components/formInput/formInput.component'

class AuthPage extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      username: '',
      password: '',
      error: ''
    }
  }

  handleChange = event => {
    const { value, name } = event.target;

    this.setState({ [name]: value });
  };



  render() {
    return (
      <div className='login-page'>
        <div className='login-container'>
          <form onSubmit={this.handleSubmit}>
            <FormInput
              name='username'
              type='username'
              handleChange={this.handleChange}
              value={this.state.username}
              label='Korisničko ime'
              required
            />
            <FormInput
              name='password'
              type='password'
              value={this.state.password}
              handleChange={this.handleChange}
              label='Lozinka'
              required
            />
            <CustomButton type='submit'> Prijava </CustomButton>
            <p>{this.state.error}</p>
          </form>
        </div>
      </div>
    );
  }
}

export default AuthPage