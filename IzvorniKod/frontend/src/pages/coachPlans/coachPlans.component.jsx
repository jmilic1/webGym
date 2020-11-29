import React from 'react'
import './coachPlans.styles.css'
import CoachPlan from "../../components/coachPlan/coachPlan.component";

class CoachPlans extends React.Component{

    constructor() {
        super();
        this.state = {
            dietPlans: []
        }
    }

    componentDidMount() {
        fetch(this.props.backendURL + "getDietPlans", {
            method: 'GET',
            //credentials: "include"
        }).then(response => {
            return response.json()
        }).then(dietPlans => {
            this.setState({
                dietPlans: dietPlans
            })
        })
    }

    render(){
        return(
            <div className='coachPlans-page-container'>
                <div className='coachPlans-dietPlans'>
                    {this.state.dietPlans.map(plan =>
                        <CoachPlan plan = {plan} />
                    )}
                </div>
            </div>
        )

    }
}

export default CoachPlans