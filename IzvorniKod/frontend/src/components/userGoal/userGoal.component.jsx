import React from 'react'
import './userGoal.styles.css'
import CustomButton from "../custom-buttom/custom-button.component";

class UserGoal extends React.Component {
    constructor(props) {
        super();
        this.state = {
            update: props.update,
            description: props.description,
            percentage: props.percentage,
        }
    }

    handleUpdateChangeClick = () => {
        this.setState({
            update: !this.state.update
        })
    }

    handleStoreGoalChangesBtnClick = () => {
        fetch(this.props.backendURL + "modifyUserGoal" , {
            method: 'POST',
            //credentials: 'include',
            body: JSON.stringify({id: this.props.id, description: this.state.description, percentage: this.state.percentage})
        }).then(response => {
            if(response.status === 200){
                alert("Promjene su spremljene")
            } else{
                alert("Došlo je do pogreške prilikom spremanja promjena")
            }
        })
    }

    handleChange = event => {
        const { value, name } = event.target;

        this.setState({ [name]: value });
    };

    render() {
        return (
            <div className='goals-container'>
                { !this.state.update ?
                    <div>
                        <p>{this.props.description}</p>
                        <p>{this.props.percentage}%</p>
                        <CustomButton onClick = {this.handleUpdateChangeClick}>Izmjeni</CustomButton>
                    </div>
                    :
                    <div>
                        <input type='text' name='description' value={this.state.description} onChange={this.handleChange}/>
                        <input type='text' name='percentage' value={this.state.percentage} onChange={this.handleChange}/>
                        <div>
                            <CustomButton onClick = {this.handleUpdateChangeClick}>Otkaži</CustomButton>
                            <CustomButton onClick = {this.handleStoreGoalChangesBtnClick}>Spremi</CustomButton>
                        </div>
                    </div>
                }
            </div>

        )
    }
}

export default UserGoal