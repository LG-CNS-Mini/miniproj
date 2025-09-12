import Book from '../component/BookComponent' ;


const books = [
   { name : "A" , price : "10,000"},
   { name : "B" , price : "20,000"},
   { name : "C" , price : "30,000"},
   { name : "D" , price : "40,000"},
   { name : "E" , price : "50,000"} 
] ;

function LibraryPage(props) {
    return (
        <div class="container">
            <div align="center">
                섭섭이와 함께하는 즐거운 SPA(React)
                <button type="button" class="btn btn-primary">Check</button>    
            </div>
            <hr/>
            {
                books.map( book => {
                    return (
                        <Book data={book} />
                    )
                })
            }
        </div>
    );
}

export default LibraryPage ;

