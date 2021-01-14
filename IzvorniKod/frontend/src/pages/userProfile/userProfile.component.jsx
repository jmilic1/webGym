import React from 'react'
import './userProfile.styles.css'
import userProfileIcon from '../../assets/userProfileIcon.svg'
import CustomButton from "../../components/custom-buttom/custom-button.component";
import UserPlan from "../../components/userPlan/userPlan.component";
import Transaction from "../../components/transaction/transaction.component";
import UserGoal from "../../components/userGoal/userGoal.component";
import BtnAdd from '../../assets/btn_add.svg'

const PLANS = "plans"
const TRANSACTIONS = 'transactions'
const GOALS = "goals"

class UserProfile extends React.Component{
    constructor(props) {
        super();
        this.state = {
            username: props.username,
            name: props.name,
            surname: props.surname,
            role: props.role,
            password: "",
            modifyUserInfo: false,
            userPlans: [],
            transactions: [],
            userGoals: [],
            view: (props.role === "OWNER" || props.role === "COACH") ? TRANSACTIONS : PLANS,
            addNewGoal: false,
            newGoalDescription: "",
            newGoalPercentage: 12
        }
    }

    componentDidMount() {
        var errorHappened = false

        if(this.props.role === "CLIENT"){
            fetch(this.props.backendURL + "userPlans" , {
                method: 'GET',
                credentials: 'include'
            }).then(response => {
                if(response.status === 200) return response.json()
                else return Promise.reject()
            }).then(plans => {
                this.setState({
                    userPlans: plans
                })
            }, function (){
                errorHappened = true
            })
        }

        fetch(this.props.backendURL + "myTransactions" , {
            method: 'GET',
            credentials: 'include'
        }).then(response => {
            if(response.status === 200) return response.json()
            else return Promise.reject()
        }).then(transactions => {
            this.setState({
                transactions: transactions
            })
        }, function (){
            errorHappened = true
        })

        if(this.props.role === "CLIENT"){
            fetch(this.props.backendURL + "getUserGoals" , {
                method: 'GET',
                credentials: 'include'
            }).then(response => {
                if(response.status === 200) return response.json()
                else return Promise.reject()
            }).then(goals => {
                this.setState({
                    userGoals: goals
                })
            }, function (){
                errorHappened = true
            })
        }
        if(errorHappened){
            alert("Došlo je do pogreške")
        }
    }

    refreshUserGoals = () => {
        fetch(this.props.backendURL + "getUserGoals" , {
            method: 'GET',
            credentials: 'include'
        }).then(response => {
            if(response.status === 200) return response.json()
            else return Promise.reject()
        }).then(goals => {
            this.setState({
                userGoals: goals
            })
        }, function(){
            alert("Došlo je do pogreške")
        })
    }

    handleChange = event => {
        const { value, name } = event.target;

        this.setState({ [name]: value });
    };

    handleChangeProfileInfoClick = () => {
        this.setState({
            modifyUserInfo: !this.state.modifyUserInfo
        })
    }

    handleDeleteUserProfileBtnClick = () => {
        if(window.confirm("Želite li obrisati profil?") === true){
            fetch(this.props.backendURL + "modifyUser" , {
                method: 'DELETE',
                credentials: 'include'
            }).then(response => {
                if(response.status === 200){
                    this.props.history.push("/logOut")
                }else if(response.status === 405){
                    alert("Ne možete izbrisati profil dok imate prijavljenu teretanu u sustavu")
                } else{
                    alert("Došlo je do pogreške prilikom brisanja profila")
                }
            })
        }
    }

    handleChangeUserInfoSubmit = () => {
        fetch(this.props.backendURL + "modifyUser" , {
            method: 'POST',
            credentials: 'include',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({name: this.state.name, surname: this.state.surname,
                                        username: this.state.username, email: this.state.email, role: this.state.role,
                                        password: this.state.password === "" ? null : this.state.password})
        })

        this.setState({
            modifyUserInfo: false
        })
    }

    handleShowPlanViewClick = () => {
        this.setState({
            view: PLANS
        })
    }
    handleShowTransactionViewClick = () => {
        this.setState({
            view: TRANSACTIONS
        })
    }
    handleShowGoalsViewClick = () => {
        this.setState({
            view: GOALS
        })
    }

    handleUserGoalPercentageChange = (e) =>{
        var number =  parseInt(e.target.value, 10)

        if((number >= 0 && number <= 100) || isNaN(number)){
            this.setState({
                newGoalPercentage: number
            })
        }
    }
    handleUserGoalDescriptionChange = (e) =>{
        this.setState({
            newGoalDescription: e.target.value
        })
    }

    handleBtnSaveNewGoalClick= () => {

        if(!isNaN(this.state.newGoalPercentage)){
            fetch(this.props.backendURL + "addUserGoal" , {
                method: 'POST',
                credentials: 'include',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({description: this.state.newGoalDescription, percentage: this.state.newGoalPercentage})
            }).then(response => {
                if(response.status === 200){
                    const newGoal = {
                        description: this.state.newGoalDescription,
                        percentCompleted: this.state.newGoalPercentage
                    }
                    const newGoalsArray = this.state.userGoals
                    newGoalsArray.push(newGoal)
                    this.setState({
                        newGoalDescription: "",
                        newGoalPercentage: 0,
                        addNewGoal: false,
                        userGoals: newGoalsArray
                    })
                    alert("Promjene su uspješno spremljene")
                    this.refreshUserGoals()
                }else{
                    alert("Došlo je do pogreške")
                }
            })
        } else{
            alert("Postotak ne može ostati prazan")
        }

    }

    handleChangeAddNewGoalFlag = () => {
        this.setState({
            addNewGoal: !this.state.addNewGoal
        })
    }

    render() {
        return (
            <div className='userProfile-page'>
                <div className='userInfo-container'>
                    <img src = {userProfileIcon} width='200px' alt='profile icon'/>
                    <div>
                        <div className='user-info-form'>
                            <div className='userInfo-formInput'>
                                <label htmlFor='name'>Ime</label>
                                <input type='text' value={this.state.name} name='name' disabled={!this.state.modifyUserInfo}
                                    onChange={this.handleChange}/>
                            </div>
                            <div className='userInfo-formInput'>
                                <label htmlFor='surname'>Prezime</label>
                                <input type='text' value={this.state.surname} name='surname' disabled={!this.state.modifyUserInfo}
                                       onChange={this.handleChange}/>
                            </div>
                            <div className='userInfo-formInput'>
                                <label htmlFor='username'>Korisničko ime</label>
                                <input type='text' value={this.state.username} name='username' disabled={true}
                                       onChange={this.handleChange}/>
                            </div>
                            {this.state.modifyUserInfo &&
                                <div className='userInfo-formInput'>
                                    <label htmlFor='password'>Lozinka (Ako ostavite prazno nećete ju promjeniti)</label>
                                    <input type='password' placeholder="Lozinka..." name='password' hidden={!this.state.modifyUserInfo}
                                           onChange={this.handleChange}/>
                                </div>
                            }
                        </div>


                        {!this.state.modifyUserInfo ?
                            <div className='modifyUser-buttons-container'>
                                <p onClick={this.handleChangeProfileInfoClick}>Uredi profil</p>
                                <button className='delete-button' onClick={this.handleDeleteUserProfileBtnClick}>Izbriši profil</button>
                            </div>
                            :
                            <div className='modifyUser-buttons-container'>
                                <CustomButton onClick = {this.handleChangeProfileInfoClick}> Otkaži </CustomButton>
                                <CustomButton onClick = {this.handleChangeUserInfoSubmit}> Potvrdi </CustomButton>
                            </div>
                        }
                    </div>
                </div>

                {(this.state.view === PLANS && (this.props.role === "CLIENT")) &&
                    <div className='userPlans-and-transactions-container'>
                        <div className='view-btns-container'>
                            <p className='active-view-btn'>Planovi</p>
                            <p className='passive-view-btn' onClick={this.handleShowTransactionViewClick}>Transakcije</p>
                            <p className='passive-view-btn' onClick={this.handleShowGoalsViewClick}>Ciljevi</p>
                        </div>
                        {
                            this.state.userPlans.map(plan =>
                                <UserPlan key = {plan.id} id={plan.id} dateOfPurchase={plan.dateOfPurchase} description={plan.description}
                                          coachUsername={plan.coachUsername} isTraining={plan.isTraining}
                                          dateFrom={plan.dateFrom}
                                          dateTo={plan.dateTo}/>)
                        }
                    </div>
                }
                {this.state.view === TRANSACTIONS &&
                    <div className='userPlans-and-transactions-container'>
                        <div className='view-btns-container'>
                            {(this.props.role === 'CLIENT')&&
                            <p className='passive-view-btn' onClick={this.handleShowPlanViewClick}>Planovi</p>
                            }
                            <p className='active-view-btn'>Transakcije</p>
                            {this.props.role === 'CLIENT' &&
                                <p className='passive-view-btn' onClick={this.handleShowGoalsViewClick}>Ciljevi</p>
                            }
                        </div>
                        {
                            this.state.transactions.map(t =>
                                <Transaction key = {t.id} amount={t.amount} dateWhen={t.dateWhen} receiverUsername={t.receiverUsername}
                                            senderUsername={t.senderUsername} transactionType={t.transactionType}/>)
                        }
                    </div>
                }
                {(this.state.view === GOALS && this.props.role === "CLIENT") &&
                    <div className='userPlans-and-transactions-container'>
                        <div className='view-btns-container'>
                            <p className='passive-view-btn' onClick={this.handleShowPlanViewClick}>Planovi</p>
                            <p className='passive-view-btn' onClick={this.handleShowTransactionViewClick}>Transakcije</p>
                            <p className='active-view-btn'>Ciljevi</p>
                        </div>
                        {this.state.addNewGoal ?
                            <div className='new-userGoal-container'>
                                <p>Dodavanje cilja</p>
                                <div className='new-userGoal-description'>
                                    <label>Opis</label>
                                    <textarea  name='newGoalDescription' value={this.state.newGoalDescription} onChange={this.handleUserGoalDescriptionChange}/>
                                </div>
                                <div className='new-userGoal-percentage'>
                                    <label>Postotak</label>
                                    <input style={{color: "black"}} type='number'  name='newGoalPercentage' value={this.state.newGoalPercentage} onChange={this.handleUserGoalPercentageChange}/>
                                </div>
                                <div className='btn-cancel-and-submit-container'>
                                    <CustomButton onClick = {this.handleChangeAddNewGoalFlag}>Otkaži</CustomButton>
                                    <CustomButton onClick = {this.handleBtnSaveNewGoalClick}>Spremi</CustomButton>
                                </div>
                            </div>
                            :
                            <div className='btnAdd-container'>
                                <img src = {BtnAdd} width='20px' alt='add btn' onClick={this.handleChangeAddNewGoalFlag}/>
                            </div>
                        }
                        {
                        this.state.userGoals.map(goal =>
                            <UserGoal refreshUserGoals = {this.refreshUserGoals} backendURL = {this.props.backendURL} id = {goal.id} key = {goal.id} description={goal.description} percentage={goal.percentCompleted} update = {false}/>)
                        }
                    </div>
                }
            </div>
        );
    }
}

export default UserProfile