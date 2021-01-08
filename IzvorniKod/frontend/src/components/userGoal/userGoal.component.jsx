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
            newPercentage: props.percentage,
            newDescription: props.description
        }
    }

    handleUpdateChangeClick = () => {
        this.setState({
            update: !this.state.update
        })
    }

    handlePercentageChange = (e) =>{
        var number =  parseInt(e.target.value, 10)

        if((number >= 0 && number <= 100) || isNaN(number)){
            this.setState({
                newPercentage: number
            })
        }
    }
    handleDescriptionChange = (e) =>{
        this.setState({
            newDescription: parseInt(e.target.value, 10)
        })
    }

    handleStoreGoalChangesBtnClick = () => {

        if(!isNaN(this.state.newPercentage)){
            fetch(this.props.backendURL + "modifyUserGoal" , {
                method: 'POST',
                credentials: 'include',
                body: JSON.stringify({id: this.props.id, description: this.state.description, percentage: this.state.newPercentage})
            }).then(response => {
                if(response.status === 200){
                    alert("Promjene su spremljene")
                } else{
                    alert("Došlo je do pogreške prilikom spremanja promjena")
                }
            })

            this.setState({
                update: false
            })
            window.location.reload()
        } else{
            alert("Postotak ne može ostati prazan")
        }
    }


    render() {
        return (
            <div className='goal-container'>
                { !this.state.update ?
                    <div className='userGoal-display-container'>
                        <textarea className='multiline'>{this.state.description}</textarea>
                        <p>{this.state.percentage}%</p>
                        <CustomButton onClick = {this.handleUpdateChangeClick}>Izmjeni</CustomButton>
                    </div>
                    :
                    <div className='userGoal-update-container'>
                        <p>Izmjena cilja</p>
                        <div className='userGoal-description-update'>
                            <label>Opis</label>
                            <textarea  name='description' value={this.state.newDescription} onChange={this.handleDescriptionChange}/>
                        </div>
                        <div className='userGoal-percentage-update'>
                            <label>Postotak</label>
                            <input type='number' max= '100' min= '0' name='percentage' value={this.state.newPercentage} onChange={this.handlePercentageChange}/>
                        </div>
                        <div className='btn-cancel-and-submit-container'>
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