import { useEffect, useState } from 'react'
import { getOne } from '../api/userApi'

const initState = {
    userId:0,
    userName:'',
    userBirth:'',
    userEmail:'',
    userPhone:'',
    department:''
}
const ReadComponent = ({userId}) => {
    const [user, setUser] = useState(initState)
    useEffect(()=>{
        getOne(userId).then(data=>{
            console.log(data);
            setUser(data)
        })
    }, [userId])
  return (
    <div>
        <table style={{border:"1px solid black", borderCollapse: "collapse", width:"400px", height:"40px", textAlign:"center"}}>
        <tr><th style={{border:"1px solid black"}}>학번</th><td style={{border:"1px solid black"}}>{user.userId}</td></tr>
        <tr><th style={{border:"1px solid black"}}>이름</th><td style={{border:"1px solid black"}}>{user.userName}</td></tr>
        <tr><th style={{border:"1px solid black"}}>생년월일</th><td style={{border:"1px solid black"}}>{user.userBirth}</td></tr>
        <tr><th style={{border:"1px solid black"}}>이메일</th><td style={{border:"1px solid black"}}>{user.userEmail}</td></tr>
        <tr><th style={{border:"1px solid black"}}>전화번호</th><td style={{border:"1px solid black"}}>{user.userPhone}</td></tr>
        <tr><th style={{border:"1px solid black"}}>학과</th><td style={{border:"1px solid black"}}>{user.department}</td></tr>
    </table>
    </div>
  )
}


export default ReadComponent