import React from 'react'
import './coachPage.syles.css'
import userProfileIcon from '../../assets/userProfileIcon.svg'
import UserPlan from "../../components/userPlan/userPlan.component";
import GymMetaDataContainerCoachPage from "../../components/gym-metadata-coachPage/gym-metadata-coachPage.component";
import CoachPlanForCoachPage from "../../components/coachPlanForCoachPage/coachPlanForCoachPage.component";

const PLANS = "PLANS"
const GYMS = "GYMS"

class CoachInfoPage extends React.Component{
    constructor(props) {
        super();
        this.state = {
            name:"",
            surname:"",
            username:"",
            email:"",
            gyms: [],
            plans: [],
            view: PLANS
        }
    }

    componentDidMount() {

        const params = {username: this.props.match.params.username};
        const searchParams = new URLSearchParams(params).toString();

        fetch(this.props.backendURL + "coach?" + searchParams.toString(), {
            method: 'GET'
            //credentials: 'include'
        }).then(response => {
            console.log(response.status)
            if(response.status === 200) return response.json()
            else return Promise.reject()
        }).then(coach => {
            this.setState({
                name: coach.user.name,
                surname: coach.user.surname,
                username: coach.user.username,
                email: coach.user.email,
                plans: coach.plans,
                gyms: coach.gyms,
                view: PLANS
            })
        }, function (){
            alert("Došlo je do pogreške")
        })
    }

    changeView = () => {
        this.setState({
            view: this.state.view === PLANS ? GYMS : PLANS
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
                                <input type='text' value={this.state.name} name='name' disabled={true}/>
                            </div>
                            <div className='userInfo-formInput'>
                                <label htmlFor='surname'>Prezime</label>
                                <input type='text' value={this.state.surname} name='surname' disabled={true}/>
                            </div>
                            <div className='userInfo-formInput'>
                                <label htmlFor='username'>Korisničko ime</label>
                                <input type='text' value={this.state.username} name='username' disabled={true}/>
                            </div>
                            <div className='userInfo-formInput'>
                                <label htmlFor='email'>Email</label>
                                <input type='text' value={this.state.email} name='username' disabled={true}/>
                            </div>
                        </div>
                    </div>
                </div>

                {this.state.view === PLANS &&
                    <div className='userPlans-and-transactions-container'>
                        <div className='view-btns-container'>
                            <p className='active-view-btn'>Planovi</p>
                            <p className='passive-view-btn' onClick={this.changeView}>Teretane</p>

                        </div>
                        {
                            this.state.plans.map(plan =>
                                <CoachPlanForCoachPage key = {plan.id} id={plan.id} dateOfPurchase={plan.dateOfPurchase} description={plan.description}
                                          coachUsername={plan.coachUsername} isTraining={plan.isTraining}
                                          dateFrom={plan.dateFrom}
                                          dateTo={plan.dateTo}
                                          loggedIn={this.props.loggedIn}/>)
                        }
                    </div>
                }

                {this.state.view === GYMS &&
                    <div className='userPlans-and-transactions-container'>
                        <div className='view-btns-container'>
                            <p className='passive-view-btn' onClick={this.changeView}>Planovi</p>
                            <p className='active-view-btn'>Teretane</p>
                        </div>
                        {
                            this.state.gyms.map(gym =>
                                <GymMetaDataContainerCoachPage  id = {gym.id} name = {gym.name} key={gym.id}/>)
                        }
                    </div>
                }
            </div>
        );
    }
}

export default CoachInfoPage