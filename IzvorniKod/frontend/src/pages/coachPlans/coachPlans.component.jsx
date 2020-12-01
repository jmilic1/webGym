import React from 'react'
import './coachPlans.styles.css'
import CoachPlan from "../../components/coachPlan/coachPlan.component";

class CoachPlans extends React.Component{

    constructor() {
        super();
        this.state = {
            dietPlans: [],
            workoutPlans:[],
            showDietPlans: true
        }
    }

    componentDidMount() {
        fetch(this.props.backendURL + "getDietPlans", {
            method: 'GET',
            //credentials: "include"
        }).then(response => {
            if(response.status === 200){
                return response.json()
            } else{
                return Promise.reject()
            }
        }).then(dietPlans => {
            this.setState({
                dietPlans: dietPlans
            })
        }, function(){
            alert("Došlo je do pogreške")
        })
    }

    setShowDietPlans = () => {
        this.setState({
            showDietPlans: true
        })
    }
    removeShowDietPlans = () => {
        this.setState({
            showDietPlans: false
        })
    }

    render(){
        return(
            <div className='coachPlans-page-container'>

                <div className='coachPlans-plansHeader'>
                    <div className={`${this.state.showDietPlans ? "marked" : ''}`} onClick={this.setShowDietPlans}><p>Planovi prehrane</p></div>
                    <div style={{textAlign: "right"}} className={`${!this.state.showDietPlans ? "marked" : ''}`} onClick={this.removeShowDietPlans}><p>Planovi treninga</p></div>
                </div>
                {this.state.showDietPlans ?
                    (<div className='coachPlans-dietPlans'>
                    {this.state.dietPlans.map(plan =>
                        <CoachPlan plan={plan}/>
                    )}
                    </div>)
                    :
                    (<div className='coachPlans-dietPlans'>
                    {this.state.workoutPlans.map(plan =>
                        <CoachPlan plan={plan}/>
                    )}
                    </div>)
                }
            </div>
        )

    }
}

export default CoachPlans