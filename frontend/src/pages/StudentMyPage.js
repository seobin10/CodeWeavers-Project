import React from 'react'
import ReadComponent from '../component/ReadComponent'
import { useParams } from 'react-router-dom'

const StudentMyPage = () => {
    const {userId} = useParams();
    console.log("userId from URL:", userId);
  return (
    <div>
    <h3>기본 정보</h3>
    <ReadComponent userId = {userId}/>
    </div>
  )
}
export default StudentMyPage