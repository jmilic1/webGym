import React from 'react'
import './gym-metadata-container.styles.scss'
import { AiOutlineEdit, AiOutlineInfoCircle, AiOutlineDeleteRow, AiOutlineDelete } from "react-icons/ai";
import { IconContext } from "react-icons";

import { Link } from 'react-router-dom'
import { Icon } from "@material-ui/core";

const GymMetaDataContainer = ({ id, name, description, email, owner = false, handleRemove }) => (
    <div className='gym-metadata-container'>
        <p className='gym-grid-content'>{name}</p>
        <p className='gym-grid-content'>{description}</p>
        <p className='gym-grid-content'>{email}</p>
        {!owner ?
            <p className='gym-grid-content'><Link to={`/gymInfo/${id}`}><AiOutlineInfoCircle /></Link></p>
            :
            <p className='gym-grid-content'>
                <div className='gym-actions'>
                    <Link to={`/gymInfo/${id}`}><AiOutlineEdit /></Link>
                    <p className='button-element'><AiOutlineDeleteRow onClick={handleRemove} /></p>
                    {/*<IconContext.Provider value={{color: 'red'}}>
                        <Link to={`/deleteGym/${id}`}><AiOutlineDelete/></Link>
                    </IconContext.Provider>*/}
                </div>
            </p>
        }

    </div>
)

export default GymMetaDataContainer
