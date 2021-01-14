import React from 'react'
import './ownerGyms.styles.scss'
import GymList from "../gymList/gymList.component";
import FormInput from "../../components/formInput/formInput.component";
import CustomButton from "../../components/custom-buttom/custom-button.component";
import GymMetaDataContainer from "../../components/gym-metadata-container/gym-metadata-container.component";

class OwnerGyms extends React.Component {
    constructor() {
        super();
        this.state = {
            gymName: '',
            gymEmail: '',
            gymDescription: '',
            gyms: [],
            addGym: false
        }
    }

    async componentDidMount() {
        await fetch(this.props.backendURL + "myGyms", {
            method: "GET",
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
        }).then(res => {
            if (!res.ok) {
                throw new Error("HTTP Error! " + res.status)
            } else {
                return res.json()
            }
        }).then(gyms => {
            this.setState({
                gyms: gyms
            })
        }
        ).catch(e => {
            alert("Došlo je do pogreške: " + e.message)
        })
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
            body: JSON.stringify({ name: this.state.gymName, description: this.state.gymDescription, email: this.state.gymEmail })
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

    handleRemoveGymClick = async (id) => {
        if (window.confirm("Želite li ukloniti teretanu sa svojeg popisa?") === true) {
            await fetch(this.props.backendURL + "myGyms", {
                method: "DELETE",
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({ id: id })
            }).then(res => {
                if (res.status === 400) {
                    throw new Error("Teretanu nije moguće ukloniti jer ste jedini voditelj u njoj!")
                } else if (!res.ok) {
                    throw new Error("HTTP error! " + res.status)
                }
            }).catch(e => {
                alert("Došlo je do pogreške, teretana nije uklonjena s popisa: " + e.message)
            })
            this.componentDidMount();
        }
    }

    handleChangeView = () => {
        if (this.state.addGym) {
            this.componentDidMount();
        }
        this.setState({
            addGym: !this.state.addGym
        })
    }

    render() {
        return (
            <div className='ownerGyms-page-container'>
                {!this.state.addGym ?
                    <div className='gymList-container'>
                        <div className='gymList-header-container'>
                            <h3>Ime teretane</h3>
                            <h3>Opis</h3>
                            <h3>E-mail</h3>
                            <h3>Mogućnosti</h3>
                        </div>
                        {this.state.gyms.map(gym =>
                            <GymMetaDataContainer ownerPage={true} handleRemove={this.handleRemoveGymClick} id={gym.id} name={gym.name} description={gym.description} email={gym.email} owner={true} key={gym.id} />
                        )}
                    </div>
                    :
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
                            <CustomButton onClick={this.handleChangeView}>Povratak na popis</CustomButton>
                        </div>
                    </fieldset>
                }
                {!this.state.addGym &&
                    <div className='toggle-button'>
                        <CustomButton onClick={this.handleChangeView}>Dodaj novu teretanu</CustomButton>
                    </div>
                }
            </div>
        )
    }
}

export default OwnerGyms;