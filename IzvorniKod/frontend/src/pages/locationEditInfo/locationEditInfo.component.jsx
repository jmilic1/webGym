import React from "react";
import './locationEditInfo.styles.scss'
import CustomButton from "../../components/custom-buttom/custom-button.component";

class LocationInfo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            country: props.location.aboutProps.country,
            street: props.location.aboutProps.street,
            opensAt: props.location.aboutProps.opensAt,
            closesAt: props.location.aboutProps.closesAt,
            city: props.location.aboutProps.city,
            phoneNumber: props.location.aboutProps.phoneNumber,
            editLocation: false
        }
    }

    async componentDidMount() {
        console.log(this.state)
    }

    handleButtonEditClick = () => {
        this.setState({
            editLocation: !this.state.editLocation
        })
    }

    handleChange = event => {
        const { value, name } = event.target;
        this.setState({ [name]: value });
    };

    handleSaveLocation = () => {
        console.log(this.state.opensAt)
        console.log(this.state.closesAt)

        fetch(this.props.backendURL + "gymLocation", {
            method: "POST",
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({
                id: this.props.match.params.id,
                street: this.state.street,
                city: this.state.city,
                country: this.state.country,
                opensAt: this.state.opensAt,
                closesAt: this.state.closesAt,
                phoneNumber: this.state.phoneNumber
            })
        }).then(res => {
            if (!res.ok) {
                throw new Error("HTTP Error! " + res.status)
            } else {
                alert("Informacije o lokaciji uspješno promijenjene!")
            }
        }).catch(e => {
            alert("Došlo je do pogreške: " + e.message)
        })
        this.handleButtonEditClick();
    }


    render() {
        return (
            <div className='location-page-container'>
                <div className='location-container'>
                    <div className='location-info-container'>
                        <div className='location-info-form'>
                            <div className='location-formInput'>
                                <label htmlFor='street'>Ulica</label>
                                <input type='text' onChange={this.handleChange} value={this.state.street} name='street'
                                    disabled={!this.state.editLocation} />
                            </div>
                            <div className='location-formInput'>
                                <label htmlFor='city'>Grad</label>
                                <input type='text' onChange={this.handleChange} value={this.state.city} name='city'
                                    disabled={!this.state.editLocation} />
                            </div>
                            <div className='location-formInput'>
                                <label htmlFor='country'>Država</label>
                                <input type='text' onChange={this.handleChange} value={this.state.country} name='country'
                                    disabled={!this.state.editLocation} />
                            </div>
                            <div className='location-formInput'>
                                <label htmlFor='opensAt'>Vrijeme otvaranja</label>
                                <input type='time' onChange={this.handleChange} value={this.state.opensAt} name='opensAt'
                                    disabled={!this.state.editLocation} />
                            </div>
                            <div className='location-formInput'>
                                <label htmlFor='closesAt'>Vrijeme zatvaranja</label>
                                <input type='time' onChange={this.handleChange} value={this.state.closesAt} name='closesAt'
                                    disabled={!this.state.editLocation} />
                            </div>
                            <div className='location-formInput'>
                                <label htmlFor='phoneNumber'>Telefonski broj</label>
                                <input type='text' onChange={this.handleChange} value={this.state.phoneNumber} name='phoneNumber'
                                    disabled={!this.state.editLocation} />
                            </div>

                        </div>
                    </div>
                    {!this.state.editLocation ?
                        <div className='button-container'>
                            <CustomButton onClick={this.handleButtonEditClick}>Uredi</CustomButton>
                        </div>
                        :
                        <div className='buttons-container'>
                            <CustomButton onClick={this.handleSaveLocation}>Spremi</CustomButton>
                            <CustomButton onClick={this.handleButtonEditClick}>Odustani</CustomButton>
                        </div>
                    }
                </div>
            </div>
        );
    }
}

export default LocationInfo