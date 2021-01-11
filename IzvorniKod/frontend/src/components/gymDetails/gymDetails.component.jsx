import React from "react";
import './gymDetails.styles.scss';
import CustomButton from "../../components/custom-buttom/custom-button.component";

class GymDetailsComponent extends React.Component {
    constructor(props) {
        super();
        this.state = {
            name: props.name,
            email: props.email,
            description: props.description,
            modifyGymInfo: false
        };
    }

    render() {
        return (
            <div>
                <h3 id='header'>Općenito</h3>
                <div className='gymDetails-container'>
                    <div className='user-info-form'>
                        <div className='userInfo-formInput'>
                            <label htmlFor='name'>Ime teretane</label>
                            <input type='text' value={this.props.name} name='name' disabled={!this.props.modifyGymInfo}/>
                        </div>
                        <div className='userInfo-formInput'>
                            <label htmlFor='email'>E-mail</label>
                            <input type='text' value={this.props.email} name='email' disabled={!this.props.modifyGymInfo}/>
                        </div>
                        <div className='userInfo-formInput'>
                            <label htmlFor='description'>Opis</label>
                            <input type='text' value={this.props.description} name='description' disabled={!this.props.modifyGymInfo}/>
                        </div>

                    </div>
                </div>

            </div>
        )
    }
}

export default GymDetailsComponent;