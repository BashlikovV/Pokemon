package by.bashlikovvv.pokemon.data.mapper

import by.bashlikovvv.pokemon.data.remote.response.TypeDto

class TypeDtoMapper : Mapper<List<TypeDto>, List<String>> {
    override fun mapFromEntity(entity: List<TypeDto>): List<String> {
        return entity.map { it.typeXDto.name }
    }

    override fun mapToEntity(domain: List<String>): List<TypeDto> {
        throw UnsupportedOperationException("Not implemented")
    }
}