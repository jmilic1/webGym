import React from 'react'
import './membership-metadata-container.styles.scss'
import { AiOutlineInfoCircle } from "react-icons/ai";
import {Link} from 'react-router-dom'

const MembershipMetadataContainer = ({id, price, description, interval}) => (
    <div className = 'membership-metadata-container'>
        <p className = 'membership-grid-content'>{description}</p>
        <p className = 'membership-grid-content'>{price}</p>
        <p className = 'membership-grid-content'>{interval}</p>
        <Link className = 'membership-grid-content' to ={`/membership/${id}`}><AiOutlineInfoCircle /></Link>
    </div>
)

export default MembershipMetadataContainer
