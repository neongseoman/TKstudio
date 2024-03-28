'use client'

import { useEffect } from "react"

function Test () {
  useEffect(() => {
    async function tt () {
      await new Promise((resolve) => setTimeout((resolve) => { 
        console.log(2)
      }, 200))
    }
    tt()
  }, [])
  
  useEffect(() => {
    console.log(1)
  }, [])

  return <></>
}

export default Test