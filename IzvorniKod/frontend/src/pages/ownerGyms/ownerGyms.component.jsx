import React from 'react'
import './ownerGyms.styles.scss'
import CustomButton from '../../components/custom-buttom/custom-button.component'
import FormInput from '../../components/formInput/formInput.component'

class OwnerGyms extends React.Component {
    constructor() {
        super();
        this.state = {
            gymName: '',
            gymEmail: ''
        }
    }

    componentDidMount() {
    }

    handleChange = event => {
        const { value, name } = event.target;
        this.setState({ [name]: value });
    };

    handleButtonInsertClick = () => {
        fetch(this.props.backendURL + "addGym", {
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({name: this.state.gymName, description: this.state.gymDescription, email: this.state.gymEmail})
        }).then(res => {
            if (!res.ok) {
                throw new Error("HTTP Error! " + res.status)
            } else {
                this.setState({
                    gymEmail: '',
                    gymDescription: '',
                    gymName: ''
                })
            }
        }).catch(e => {
            alert("Došlo je do pogreške: " + e.message)
        })
    }

    render() {
        return (
            <div className='ownerGyms-page-container'>
                <fieldset>
                    <legend>Unos nove teretane</legend>
                    <div className='insert-gym-container'>
                        <FormInput
                            id='name'
                            name='gymName'
                            type='name'
                            handleChange={this.handleChange}
                            value={this.state.gymName}
                            label='Ime teretane'
                            required
                        />
                        <FormInput
                            id='description'
                            name='gymDescription'
                            type='description'
                            handleChange={this.handleChange}
                            value={this.state.gymDescription}
                            label='Opis'
                            required
                        />
                        <FormInput
                            id='email'
                            name='gymEmail'
                            type='email'
                            handleChange={this.handleChange}
                            value={this.state.gymEmail}
                            label='E-mail teretane'
                            required
                        />
                    </div>
                    <div className='button-container'>
                        <CustomButton onClick={this.handleButtonInsertClick}>Dodaj teretanu</CustomButton>
                    </div>
                </fieldset>
            </div>
        )
    }
}

export default OwnerGyms