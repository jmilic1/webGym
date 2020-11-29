import React from 'react'
import './coachPlan.styles.css'

const CoachPlan = ({plan}) => {
    return(
        <div className='coachPlan-container'>
            <p>{plan.description}</p>
            <p>{plan.price} kn</p>
        </div>
    )
}

export default CoachPlan