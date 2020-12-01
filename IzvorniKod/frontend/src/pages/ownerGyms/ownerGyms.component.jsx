import React from 'react'
import './ownerGyms.styles.scss'
import CustomButton from '../../components/custom-buttom/custom-button.component'
import FormInput from '../../components/formInput/formInput.component'

class OwnerGyms extends React.Component {
    constructor() {
        super();
        this.state = {
            gymName: '',
            gymCity: '',
            gyms: []
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
            body: JSON.stringify({name: this.state.gymName, city: this.state.gymCity})
        }).then(res => {
            if (!res.ok) {
                throw new Error("HTTP Error! " + res.status)
            } else {
                document.getElementById("textfield1").value = ""
                document.getElementById("textfield2").value = ""
                this.state.gymCity = ''
                this.state.gymName = ''
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
                            id='textfield1'
                            name='gymName'
                            type='name'
                            handleChange={this.handleChange}
                            value={this.state.gymName}
                            label='Ime teretane'
                            required
                        />
                        <FormInput
                            id='textfield2'
                            name='gymCity'
                            type='city'
                            handleChange={this.handleChange}
                            value={this.state.gymCity}
                            label='Grad u kojem se teretana nalazi'
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