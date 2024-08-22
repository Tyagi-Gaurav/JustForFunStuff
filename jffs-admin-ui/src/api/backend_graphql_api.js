import { ApolloClient, InMemoryCache, gql } from "@apollo/client";

export const apolloClient = new ApolloClient({
  uri: "/admin/graphql",
  cache: new InMemoryCache(),
});

export const getWordsGraphQL = (pageNum) => {
  return apolloClient.query({
    query: gql`
            query GetAllWords {
                allWords(pageNum: ${pageNum}) {
                    words {
                        word
                        meanings {
                            definition
                            synonyms
                            examples
                        }
                    }
                    totalWords
                    totalPages
                    currentPage
                    nextPage
                    previousPage
                }
            }`,
  });
};

export const searchGraphQL = (searchType, searchValue) => {
  return apolloClient.query({
    query: gql`
            query searchBy {
              search(searchType : ${searchType}, searchValue: "${searchValue}") {
                word
                meanings {
                    synonyms
                }
            }
        }`,
  });
};

export const updateWordGraphQL = (oldWord, word) => {
    return apolloClient.mutate({
        query: gql`
                query searchBy {
                  search(searchType : ${searchType}, searchValue: "${searchValue}") {
                    word
                    meanings {
                        synonyms
                    }
                }
            }`,
      });

    return axios.put("/admin/v1/words/" + oldWord, word, {
      headers: {
        "Content-Type": "application/vnd+update.word.v1+json",
      },
    });
  };